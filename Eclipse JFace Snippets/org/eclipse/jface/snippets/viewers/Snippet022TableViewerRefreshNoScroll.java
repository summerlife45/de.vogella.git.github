/*******************************************************************************
 * Copyright (c) 2006 Tom Schindl and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl - initial API and implementation
 *******************************************************************************/

package org.eclipse.jface.snippets.viewers;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * A simple TableViewer to demonstrating how viewers could be refresh and
 * scrolling avoided in 3.2. In 3.3 implementors should consider using the
 * {@link StructuredViewer#refresh(boolean, boolean)} instead.
 * 
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 * 
 */
public class Snippet022TableViewerRefreshNoScroll {
	private class MyContentProvider implements IStructuredContentProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			ArrayList list = (ArrayList) inputElement;
			list.add(new MyModel(list.size()));
			return list.toArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}

	public class MyModel {
		public int counter;

		public MyModel(int counter) {
			this.counter = counter;
		}

		public String toString() {
			return "Item " + this.counter;
		}
	}

	public Snippet022TableViewerRefreshNoScroll(Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		final TableViewer v = new TableViewer(shell, SWT.BORDER
				| SWT.FULL_SELECTION);

		TableColumn column = new TableColumn(v.getTable(), SWT.NONE);
		column.setWidth(200);

		v.setLabelProvider(new LabelProvider());
		v.setContentProvider(new MyContentProvider());
		v.setInput(createModel(100));
		v.getTable().setLinesVisible(true);
		v.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Button b = new Button(shell, SWT.PUSH);
		b.setText("Refresh with Scrolling");
		b.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				v.refresh();
			}

		});

		b = new Button(shell, SWT.PUSH);
		b.setText("Refresh with NO-Scrolling");
		b.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				v.getTable().setTopIndex(0);
				IStructuredSelection selection = (IStructuredSelection) v
						.getSelection();
				v.getTable().deselectAll();
				v.refresh();
				if (!selection.isEmpty()) {
					int[] indices = new int[selection.size()];

					Iterator it = selection.iterator();
					TableItem[] items = v.getTable().getItems();
					Object modelElement;

					int counter = 0;
					while (it.hasNext()) {
						modelElement = it.next();
						for (int i = 0; i < items.length; i++) {
							if (items[i].getData() == modelElement) {
								indices[counter++] = i;
							}
						}
					}

					if (counter < indices.length) {
						System.arraycopy(items, 0, indices = new int[counter],
								0, counter);
					}

					v.getTable().select(indices);
				}
			}

		});
	}

	private ArrayList createModel(int size) {
		ArrayList elements = new ArrayList();

		for (int i = 0; i < size; i++) {
			elements.add(new MyModel(i));
		}

		return elements;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		new Snippet022TableViewerRefreshNoScroll(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
