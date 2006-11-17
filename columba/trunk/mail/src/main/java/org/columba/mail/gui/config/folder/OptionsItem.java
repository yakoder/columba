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
package org.columba.mail.gui.config.folder;

import javax.swing.Icon;

import org.columa.core.config.IDefaultItem;
import org.columba.core.config.DefaultItem;
import org.columba.core.xml.XmlElement;
import org.frapuccino.checkablelist.CheckableItem;

/**
 * @author fdietz
 */
public class OptionsItem implements CheckableItem {

	private XmlElement element;

	public OptionsItem(XmlElement element) {
		this.element = element;
	}

	public void setSelected(boolean b) {
		element.addAttribute("overwrite", Boolean.toString(b));
	}

	/**
	 * @see org.columba.core.gui.checkablelist.CheckableItem#isSelected()
	 */
	public boolean isSelected() {
		IDefaultItem item = new DefaultItem(element);

		return item.getBooleanWithDefault("overwrite", false);
	}

	/**
	 * @see org.columba.core.gui.checkablelist.CheckableItem#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}

	public String toString() {
		return element.getName();
	}

	/**
	 * @return
	 */
	public XmlElement getElement() {
		return element;
	}
}
