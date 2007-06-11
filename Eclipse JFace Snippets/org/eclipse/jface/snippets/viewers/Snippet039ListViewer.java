package org.eclipse.jface.snippets.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Demonstrate a simple ListViewer
 */
public class Snippet039ListViewer {
	private class MyContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			return (MyModel[])inputElement;
		}

		public void dispose() {
			
		}

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
	
	public Snippet039ListViewer(Shell shell) {
		final ListViewer v = new ListViewer(shell,SWT.H_SCROLL|SWT.V_SCROLL);
		v.setLabelProvider(new LabelProvider());
		v.setContentProvider(new MyContentProvider());
		MyModel[] model = createModel();
		v.setInput(model);	
	}
	
	private MyModel[] createModel() {
		MyModel[] elements = new MyModel[10];
		
		for( int i = 0; i < 10; i++ ) {
			elements[i] = new MyModel(i);
		}
		
		return elements;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet039ListViewer(shell);
		shell.open ();
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		
		display.dispose ();

	}

}