// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.tree.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.FolderFactory;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.config.search.SearchFrame;
import org.columba.mail.gui.frame.AbstractMailFrameController;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.gui.tree.selection.TreeSelectionChangedEvent;
import org.columba.mail.gui.tree.util.CreateFolderDialog;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author frd
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class CreateVirtualFolderAction extends AbstractColumbaAction implements
		ISelectionListener {

	public CreateVirtualFolderAction(IFrameMediator frameMediator) {
		super(frameMediator, MailResourceLoader.getString("menu", "mainframe",
				"menu_folder_newvirtualfolder"));

		// tooltip text
		putValue(SHORT_DESCRIPTION, MailResourceLoader.getString("menu",
				"mainframe", "menu_folder_newvirtualfolder")
				.replaceAll("&", ""));

		// icons
		putValue(SMALL_ICON, ImageLoader.getSmallIcon("folder-saved-search.png"));
		putValue(LARGE_ICON, ImageLoader.getIcon("folder-saved-search.png"));

		// shortcut key
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));

		setEnabled(false);

		((MailFrameMediator) frameMediator).registerTreeSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		CreateFolderDialog dialog = new CreateFolderDialog(getFrameMediator(),
				null);

		String name;

		if (dialog.success()) {
			// ok pressed
			name = dialog.getName();

			try {
				MailFolderCommandReference r = (MailFolderCommandReference) ((AbstractMailFrameController) getFrameMediator())
						.getTreeSelection();

				VirtualFolder vfolder = (VirtualFolder) FolderFactory
						.getInstance().createChild((IMailFolder) r.getSourceFolder(), name,
								"VirtualFolder");

				FolderTreeModel.getInstance().nodeStructureChanged((IMailFolder)r.getSourceFolder());

				// set parent folder uid
				vfolder.getConfiguration().setString("property", "source_uid",
						r.getSourceFolder().getId());

				// open search dialog
				new SearchFrame((AbstractMailFrameController) frameMediator,
						vfolder);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			// cancel pressed
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.gui.util.ISelectionListener#selectionChanged(org.columba.core.gui.util.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent e) {
		if (((TreeSelectionChangedEvent) e).getSelected().length > 0) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
}