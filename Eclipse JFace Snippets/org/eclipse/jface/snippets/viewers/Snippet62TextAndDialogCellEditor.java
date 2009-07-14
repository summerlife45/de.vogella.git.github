/*******************************************************************************
 * Copyright (c) 2006 Eric Rizzo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eric Rizzo - initial implementation
 *******************************************************************************/

package org.eclipse.jface.snippets.viewers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Demonstrates usage of {@link TextAndDialogCellEditor}. The email column uses the
 * TextAndDialogCellEditor; othe columns use ordinary {@link TextCellEditor}s.
 * 
 * @author Eric Rizzo
 * 
 */
public class Snippet62TextAndDialogCellEditor {

	private class MyContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			return (Person[]) inputElement;
		}

		public void dispose() {
			// noting to do
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// noting to do
		}

	}

	public class Person {
		public String givenname;
		public String surname;
		public String email;

		public Person(String givenname, String surname, String email) {
			this.givenname = givenname;
			this.surname = surname;
			this.email = email;
		}

		public String toString() {
			return '[' + givenname + ' ' + surname + ' ' + email + ']';
		}
	}

	protected abstract class AbstractEditingSupport extends EditingSupport {
		private CellEditor editor;

		public AbstractEditingSupport(TableViewer viewer, CellEditor anEditor) {
			super(viewer);
			this.editor = anEditor;
		}

		protected boolean canEdit(Object element) {
			return editor != null;
		}

		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		protected void setValue(Object element, Object value) {
			doSetValue(element, value);
			getViewer().update(element, null);
		}

		protected abstract void doSetValue(Object element, Object value);
	}

	public Snippet62TextAndDialogCellEditor(Shell shell) {
		TableViewer v = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		v.setContentProvider(new MyContentProvider());

		TableViewerColumn column = new TableViewerColumn(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Givenname");
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {

			public String getText(Object element) {
				return ((Person) element).givenname;
			}
		});

		column.setEditingSupport(new AbstractEditingSupport(v, new TextCellEditor(v.getTable())) {

			protected Object getValue(Object element) {
				return ((Person) element).givenname;
			}

			protected void doSetValue(Object element, Object value) {
				((Person) element).givenname = value.toString();
			}

		});

		column = new TableViewerColumn(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Surname");
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {

			public String getText(Object element) {
				return ((Person) element).surname;
			}

		});

		column.setEditingSupport(new AbstractEditingSupport(v, new TextCellEditor(v.getTable())) {
			protected Object getValue(Object element) {
				return ((Person) element).surname;
			}

			protected void doSetValue(Object element, Object value) {
				((Person) element).surname = value.toString();
			}

		});

		column = new TableViewerColumn(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("E-Mail");
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((Person) element).email;
			}

		});
		

		TextAndDialogCellEditor cellEditor = new TextAndDialogCellEditor(v.getTable());
		cellEditor.setDialogMessage("Enter email address");
		column.setEditingSupport(new AbstractEditingSupport(v, cellEditor) {

			protected Object getValue(Object element) {
				return ((Person) element).email;
			}

			protected void doSetValue(Object element, Object value) {
				((Person) element).email = value.toString();
			}

			// Print out the model after each edit to verify its values are updated correctly
			protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
				super.saveCellEditorValue(cellEditor, cell);
				System.out.println(cell.getElement());
			}
		});

		Person[] model = createModel();
		v.setInput(model);
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);
	}

	private Person[] createModel() {
		Person[] elements = new Person[4];
		elements[0] = new Person("Tom", "Schindl",
				"tom.schindl@bestsolution.at");
		elements[1] = new Person("Boris", "Bokowski",
				"Boris_Bokowski@ca.ibm.com");
		elements[2] = new Person("Tod", "Creasey", "Tod_Creasey@ca.ibm.com");
		elements[3] = new Person("Wayne", "Beaton", "wayne@eclipse.org");

		return elements;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet62TextAndDialogCellEditor(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
