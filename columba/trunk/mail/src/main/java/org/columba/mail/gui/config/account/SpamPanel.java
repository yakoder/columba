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

package org.columba.mail.gui.config.account;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.base.CheckBoxWithMnemonic;
import org.columba.core.gui.base.MultiLineLabel;
import org.columba.core.gui.base.RadioButtonWithMnemonic;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.SpamItem;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.gui.tree.util.SelectFolderDialog;
import org.columba.mail.gui.tree.util.TreeNodeList;
import org.columba.mail.util.MailResourceLoader;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Panel for spam options.
 * 
 * @author fdietz
 */
public class SpamPanel extends DefaultPanel implements ActionListener {

	private AccountItem item;

	private JCheckBox enableCheckBox;

	private JCheckBox addressCheckBox;

	private JCheckBox incomingCheckBox;

	private JCheckBox markCheckBox;

	private JRadioButton incomingTrashRadioButton;

	private JRadioButton incomingMoveToRadioButton;

	private JButton incomingChooseFolderButton;

	private JRadioButton markTrashRadioButton;

	private JRadioButton markMoveToRadioButton;

	private JButton markChooseFolderButton;

	private MultiLineLabel label;

	private IFrameMediator mediator;

	public SpamPanel(IFrameMediator mediator, AccountItem item) {
		this.item = item;
		this.mediator = mediator;

		initComponents();
		updateComponents(true);
		layoutComponents();
	}

	protected void initComponents() {
		enableCheckBox = new CheckBoxWithMnemonic(MailResourceLoader.getString(
				"dialog", "account", "enable_filter"));
		enableCheckBox.setActionCommand("ENABLE");
		enableCheckBox.addActionListener(this);

		addressCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "account", "dont_mark_message"));

		incomingCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "account", "move_incoming_messages"));
		incomingCheckBox.setActionCommand("INCOMING");
		incomingCheckBox.addActionListener(this);

		incomingChooseFolderButton = new ButtonWithMnemonic("Inbox");
		incomingChooseFolderButton.setActionCommand("INCOMING_BUTTON");
		incomingChooseFolderButton.addActionListener(this);

		incomingTrashRadioButton = new RadioButtonWithMnemonic(
				MailResourceLoader.getString("dialog", "account",
						"trash_folder"));
		incomingMoveToRadioButton = new RadioButtonWithMnemonic(
				MailResourceLoader.getString("dialog", "account", "move_to"));
		ButtonGroup group = new ButtonGroup();
		group.add(incomingTrashRadioButton);
		group.add(incomingMoveToRadioButton);

		markCheckBox = new CheckBoxWithMnemonic(MailResourceLoader.getString(
				"dialog", "account", "when_marking_message"));
		markCheckBox.setActionCommand("MARK");
		markCheckBox.addActionListener(this);

		markTrashRadioButton = new RadioButtonWithMnemonic(MailResourceLoader
				.getString("dialog", "account", "move_to_trash_folder"));
		markMoveToRadioButton = new RadioButtonWithMnemonic(MailResourceLoader
				.getString("dialog", "account", "move_to"));
		ButtonGroup group2 = new ButtonGroup();
		group2.add(markTrashRadioButton);
		group2.add(markMoveToRadioButton);

		markChooseFolderButton = new ButtonWithMnemonic("Inbox");
		markChooseFolderButton.setActionCommand("MARK_BUTTON");
		markChooseFolderButton.addActionListener(this);

		label = new MultiLineLabel(MailResourceLoader.getString("dialog",
				"account", "spam_intro"));
	}

	protected void layoutComponents() {
		// Create a FormLayout instance.
		FormLayout layout = new FormLayout(
				"10dlu, 10dlu, max(100;default), 3dlu, max(150dlu;default):grow, 3dlu",

				// 2 columns
				""); // rows are added dynamically (no need to define them

		// here)
		DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
		builder.setLeadingColumnOffset(1);

		// create EmptyBorder between components and dialog-frame
		builder.setDefaultDialogBorder();

		builder.setLeadingColumnOffset(1);

		builder.appendSeparator(MailResourceLoader.getString("dialog",
				"account", "separator_adaptive_spam_filter"));

		builder.append(label, 4);
		builder.nextLine();

		builder.append(enableCheckBox, 4);
		builder.nextLine();

		builder.appendSeparator(MailResourceLoader.getString("dialog",
				"account", "separator_filter_options"));
		builder.nextLine();

		builder.append(addressCheckBox, 4);
		builder.nextLine();

		builder.append(incomingCheckBox, 4);
		builder.nextLine();

		builder.setLeadingColumnOffset(2);

		builder.append(incomingTrashRadioButton, 3);
		builder.nextLine();
		builder.append(incomingMoveToRadioButton, 1);
		builder.append(incomingChooseFolderButton, 2);

		builder.setLeadingColumnOffset(1);

		builder.append(markCheckBox, 4);
		builder.nextLine();

		builder.setLeadingColumnOffset(2);

		builder.append(markTrashRadioButton, 3);
		builder.nextLine();

		builder.append(markMoveToRadioButton, 1);
		builder.append(markChooseFolderButton, 2);
	}

	public void updateComponents(boolean b) {
		SpamItem spam = item.getSpamItem();

		if (b) {
			enableCheckBox.setSelected(spam.isEnabled());

			incomingCheckBox.setSelected(spam
					.isMoveIncomingJunkMessagesEnabled());

			IMailbox folder = (IMailbox) FolderTreeModel.getInstance()
					.getFolder(spam.getIncomingCustomFolder());
			String treePath = folder.getTreePath();
			incomingChooseFolderButton.setText(treePath);

			incomingMoveToRadioButton.setSelected(!spam
					.isIncomingTrashSelected());

			incomingTrashRadioButton
					.setSelected(spam.isIncomingTrashSelected());

			markCheckBox.setSelected(spam.isMoveMessageWhenMarkingEnabled());

			folder = (IMailbox) FolderTreeModel.getInstance().getFolder(
					spam.getMoveCustomFolder());
			treePath = folder.getTreePath();
			markChooseFolderButton.setText(treePath);

			markMoveToRadioButton.setSelected(!spam.isMoveTrashSelected());
			markTrashRadioButton.setSelected(spam.isMoveTrashSelected());
			addressCheckBox.setSelected(spam.checkAddressbook());

			enableComponents(enableCheckBox.isSelected());
		} else {
			spam.setEnabled(enableCheckBox.isSelected());

			spam.enableMoveIncomingJunkMessage(incomingCheckBox.isSelected());
			spam.enableMoveMessageWhenMarking(markCheckBox.isSelected());

			spam.selectedIncomingTrash(incomingTrashRadioButton.isSelected());
			spam.selectMoveTrash(markTrashRadioButton.isSelected());

			TreeNodeList list = new TreeNodeList(incomingChooseFolderButton
					.getText());
			IMailbox folder = (IMailbox) FolderTreeModel.getInstance()
					.getFolder(list);

			if (folder == null) {
				// user didn't select any folder
				// -> make Inbox the default folder
				folder = (IMailbox) FolderTreeModel.getInstance()
						.getFolder("101");
			}

			String uid = folder.getId();
			spam.setIncomingCustomFolder(uid);

			list = new TreeNodeList(markChooseFolderButton.getText());
			folder = (IMailbox) FolderTreeModel.getInstance().getFolder(list);

			if (folder == null) {
				// user didn't select any folder
				// -> make Inbox the default folder
				folder = (IMailbox) FolderTreeModel.getInstance()
						.getFolder("101");
			}

			uid = folder.getId();
			spam.setMoveCustomFolder(uid);
			spam.enableCheckAddressbook(addressCheckBox.isSelected());
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		String action = arg0.getActionCommand();

		if (action.equals("ENABLE")) {
			enableComponents(enableCheckBox.isSelected());
		} else if (action.equals("INCOMING")) {
			enableIncoming(incomingCheckBox.isSelected());
		} else if (action.equals("MARK")) {
			enableMark(markCheckBox.isSelected());
		} else if (action.equals("MARK_BUTTON")) {
			SelectFolderDialog dialog = new SelectFolderDialog(mediator);
			if (dialog.success()) {
				IMailFolder folder = (IMailFolder) dialog.getSelectedFolder();

				String treePath = folder.getTreePath();
				markChooseFolderButton.setText(treePath);
			}
		} else if (action.equals("INCOMING_BUTTON")) {
			SelectFolderDialog dialog = new SelectFolderDialog(mediator);
			if (dialog.success()) {
				IMailFolder folder = (IMailFolder) dialog.getSelectedFolder();

				String treePath = folder.getTreePath();
				incomingChooseFolderButton.setText(treePath);
			}
		}
	}

	private void enableComponents(boolean enable) {
		addressCheckBox.setEnabled(enable);

		incomingCheckBox.setEnabled(enable);
		enableIncoming(enable);

		markCheckBox.setEnabled(enable);
		enableMark(enable);
	}

	private void enableIncoming(boolean enable) {
		incomingChooseFolderButton.setEnabled(incomingCheckBox.isSelected());
		incomingMoveToRadioButton.setEnabled(incomingCheckBox.isSelected());
		incomingTrashRadioButton.setEnabled(incomingCheckBox.isSelected());
	}

	private void enableMark(boolean enable) {
		markChooseFolderButton.setEnabled(markCheckBox.isSelected());
		markMoveToRadioButton.setEnabled(markCheckBox.isSelected());
		markTrashRadioButton.setEnabled(markCheckBox.isSelected());
	}
}
