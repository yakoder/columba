//The contents of this file are subject to the Mozilla Public License Version 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.gui.tree.util;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.columba.core.resourceloader.ImageLoader;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.IMailboxInfo;
import org.columba.mail.folder.LocalRootFolder;
import org.columba.mail.folder.imap.IMAPRootFolder;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.resourceloader.MailImageLoader;

/**
 * This class is used for the mail folder tree.
 */
public class FolderTreeCellRenderer extends DefaultTreeCellRenderer {
	private static Icon expandedFolderIcon;

	private static Icon collapsedFolderIcon;

	private static Icon virtualFolderIcon;

	private static Icon localRootFolderIcon;

	private static Icon imapRootFolderIcon;

	private static Icon inboxIcon;

	private static Icon outboxIcon;

	private static Icon trashIcon;

	private static Icon templateIcon;

	private static Icon sentIcon;

	static {
		collapsedFolderIcon = ImageLoader.getSmallIcon("folder.png");
		expandedFolderIcon = ImageLoader.getSmallIcon("folder-open.png");
		virtualFolderIcon = ImageLoader.getSmallIcon("folder-saved-search.png");
		localRootFolderIcon = ImageLoader.getSmallIcon("computer.png");
		imapRootFolderIcon = ImageLoader.getSmallIcon("network-server.png");
		inboxIcon = MailImageLoader.getSmallIcon("folder-inbox-new.png");
		outboxIcon = MailImageLoader.getSmallIcon("folder-outbox.png");

		templateIcon = MailImageLoader.getSmallIcon("folder-template.png");
		sentIcon = MailImageLoader.getSmallIcon("folder-sent.png");
		trashIcon = ImageLoader.getSmallIcon("user-trash.png");
	}

	private Font plainFont;

	private Font boldFont;

	private Font italicFont;

	/**
	 * Generates a new CellRenderer. In this contructor font and images are set
	 * to local variables. The fonts are depended on the current UIManager.
	 */
	public FolderTreeCellRenderer() {
		super();

		boldFont = UIManager.getFont("Tree.font");
		boldFont = boldFont.deriveFont(Font.BOLD);
		italicFont = UIManager.getFont("Tree.font");
		italicFont = italicFont.deriveFont(Font.ITALIC);
		plainFont = UIManager.getFont("Tree.font");
	}

	/**
	 * The tooltip text and unseen counter for the current folder component are
	 * set. If the folder has unseen Messages the folder self is show as bold
	 * and the unseen message counter is added to the folder label. The folder
	 * gets a tooltip where infos (unseen, recent, total) are set. If the folder
	 * is an IMAP folder and not selectable the folder is set to italic with a
	 * darkgrey background.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row,
			boolean hasFocusVar) {
		/*
		 * RIYAD: Even though we don't do anything with this value, what it is
		 * doing is setting up the selection colors and such as implemented per
		 * the default cell rendered.
		 */
		super.getTreeCellRendererComponent(tree, value, isSelected, expanded,
				leaf, row, hasFocusVar);

		// setting default Values
		// setFont(plainFont);
		// setToolTipText("");

		IMailFolder treeNode = (IMailFolder) value;
		// setText(treeNode.getName());
		// setIcon(getFolderIcon(treeNode, expanded));

		if (value instanceof IMailbox) {
			IMailbox folder = (IMailbox) value;

			// getting folder info
			IMailboxInfo info = folder.getMessageFolderInfo();

			// get folder item configuration
			IFolderItem item = folder.getConfiguration();

			if (item == null)
				return this;

			// set tooltip text
			setToolTipText(createTooltipText(info));

			// set label text
			setText(createLabelText(info, item));

		} else {
			setText(treeNode.getName());
		}

		setIcon(getFolderIcon(treeNode, expanded));
		return this;
	}

	/**
	 * Create HTML label text
	 *
	 * @param info
	 *            mailbox info (total/unread/recent count)
	 * @param item
	 *            folderitem containing xml-configuration
	 * @return label
	 */
	private String createLabelText(IMailboxInfo info, IFolderItem item) {

		// name of folder
		String name = item.getString("property", "name");
		// IMAP folder specific
		// - Is this folder selectable (does it contain messages?)
		boolean not_selectable = item.getBooleanWithDefault("selectable", true) == false;

		if (not_selectable || info.getUnseen() > 0 || info.getRecent() > 0) {

			StringBuffer buf;
			// set label text
			buf = new StringBuffer("<html><body>");

			// if IMAP selectable folder
			// -> lightgray font color and italic
			if (not_selectable)
				buf.append("<i><font color='#333333'>");

			if (info.getUnseen() > 0) {
				// draw folder name and unseen count
				// -> using bold font
				buf.append("<b>");

				// append unseen count to folder name
				name = name + " (" + info.getUnseen() + ")";
				buf.append(name);

				buf.append("</b>");
			} else
				buf.append(name);

			// if IMAP selectable folder
			if (not_selectable)
				buf.append("</i>");

			if (info.getRecent() > 0) {
				// append recent message count
				// -> using blue font
				buf.append("<font color='blue'> (");
				buf.append(info.getRecent());
				buf.append(")");
			}

			buf.append("</body></html>");

			return buf.toString();
		} else {
			return name;
		}
	}

	/**
	 * Create HTML tooltip text.
	 *
	 * @param info
	 *            mailboxinfo (total/unread/recent count)
	 * @return tooltip text
	 */
	private String createTooltipText(IMailboxInfo info) {
		StringBuffer buf = new StringBuffer();
		buf.append("<html><body>&nbsp;Total: " + info.getExists());
		buf.append("<br>&nbsp;Unseen: " + info.getUnseen());
		buf.append("<br>&nbsp;Recent: " + info.getRecent());
		buf.append("</body></html>");
		return buf.toString();
	}

	/**
	 * Returns an icon suitable for the given folder.
	 */
	public static Icon getFolderIcon(IMailFolder node, boolean expanded) {
		if (node instanceof LocalRootFolder) {
			return localRootFolderIcon;
		} else if (node instanceof IMAPRootFolder) {
			return imapRootFolderIcon;
		} else if (node instanceof VirtualFolder) {
			return virtualFolderIcon;
		} else if (node instanceof IMailbox) {
			IMailbox folder = (IMailbox) node;
			if (folder.isInboxFolder()) {
				return inboxIcon;
			} else if (folder.getId().equals("103")) {

				return outboxIcon;
			} else if (folder.getId().equals("107")) {
				return templateIcon;
			} else if (folder.getId().equals("104")) {
				return sentIcon;
			} else if (folder.isTrashFolder()) {
				return trashIcon;
			}
		}
		return expanded ? expandedFolderIcon : collapsedFolderIcon;
	}
}