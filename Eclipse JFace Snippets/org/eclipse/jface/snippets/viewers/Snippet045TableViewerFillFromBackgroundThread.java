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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Example how to fill a {@link TableViewer} from the background without
 * blocking the UI
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet045TableViewerFillFromBackgroundThread {
	private static int COUNTER = 0;

	private class MyContentProvider implements IStructuredContentProvider {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return ((List) inputElement).toArray();
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

	public class MyLabelProvider extends LabelProvider implements
			ITableLabelProvider, ITableFontProvider, ITableColorProvider {
		FontRegistry registry = new FontRegistry();

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return "Column " + columnIndex + " => " + element.toString();
		}

		public Font getFont(Object element, int columnIndex) {
			if (((MyModel) element).counter % 2 == 0) {
				return registry.getBold(Display.getCurrent().getSystemFont()
						.getFontData()[0].getName());
			}
			return null;
		}

		public Color getBackground(Object element, int columnIndex) {
			if (((MyModel) element).counter % 2 == 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
			return null;
		}

		public Color getForeground(Object element, int columnIndex) {
			if (((MyModel) element).counter % 2 == 1) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
			return null;
		}

	}

	public Snippet045TableViewerFillFromBackgroundThread(final Shell shell) {
		final TableViewer v = new TableViewer(shell, SWT.BORDER
				| SWT.FULL_SELECTION);
		v.setLabelProvider(new MyLabelProvider());
		v.setContentProvider(new MyContentProvider());

		TableColumn column = new TableColumn(v.getTable(), SWT.NONE);
		column.setWidth(200);
		column.setText("Column 1");

		column = new TableColumn(v.getTable(), SWT.NONE);
		column.setWidth(200);
		column.setText("Column 2");

		final ArrayList model = new ArrayList();
		v.setInput(model);
		v.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				MyModel m1 = (MyModel) e1;
				MyModel m2 = (MyModel) e2;
				return m2.counter - m1.counter;
			}

		});
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);

		TimerTask task = new TimerTask() {

			public void run() {
				shell.getDisplay().syncExec(new Runnable() {

					public void run() {
						MyModel el = new MyModel(++COUNTER);
						v.add(el);
						model.add(el);
					}
				});
			}
		};

		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet045TableViewerFillFromBackgroundThread(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
