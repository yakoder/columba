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
package org.columba.addressbook.model;

import org.columba.core.xml.XmlElement;

/**
 * @author fdietz
 *  
 */
public class GroupModel implements IGroupModel {

	private XmlElement root;
	private XmlElement property;
	private String folderUid;
	
	public GroupModel(XmlElement group, XmlElement property, String folderUid) {
		this.root = group;
		this.property = property;
		this.folderUid = folderUid;

	}

	public GroupModel() {

		root = new XmlElement("group");
		property = new XmlElement("property");
		XmlElement members = new XmlElement("members");

		property.addElement(root);
		root.addElement(members);
	}

	public XmlElement getRootElement() {
		return root;
	}

	public void setName(String name) {
		property.addAttribute("name", name);

	}

	public String getName() {
		return property.getAttribute("name");
	}

	public void setDescription(String description) {
		root.addAttribute("description", description);
	}

	public String getDescription() {
		return root.getAttribute("description");
	}

	public void addMember(Object uid) {

		XmlElement members = root.getElement("members");

		XmlElement m = new XmlElement("member");
		m.addAttribute("uid", uid.toString());

		members.addElement(m);
	}

	public int count() {
		XmlElement members = root.getElement("members");

		return members.count();
	}

	public boolean exists(Object uid) {
		if (getMember(uid) != null)
			return true;

		return false;
	}

	protected XmlElement getMember(Object uid) {
		XmlElement members = root.getElement("members");

		for (int i = 0; i < members.count(); i++) {
			XmlElement m = members.getElement(i);
			String str = m.getAttribute("uid");
			if (str.equals(uid.toString()))
				return m;
		}

		return null;
	}

	public String[] getMembers() {
		XmlElement members = root.getElement("members");

		String[] result = new String[members.count()];
		for (int i = 0; i < members.count(); i++) {
			XmlElement m = members.getElement(i);
			String uid = m.getAttribute("uid");
			result[i] = uid;
		}

		return result;
	}

	public void remove(Object uid) {
		XmlElement m = getMember(uid);

		if (m != null)
			m.removeFromParent();
	}

	public void removeAllMembers() {
		XmlElement members = root.getElement("members");
		members.removeAllElements();
	}

	/**
	 * @return Returns the folderUid.
	 */
	public String getFolderUid() {
		return folderUid;
	}
	/**
	 * @param folderUid The folderUid to set.
	 */
	public void setFolderUid(String folderUid) {
		this.folderUid = folderUid;
	}
}