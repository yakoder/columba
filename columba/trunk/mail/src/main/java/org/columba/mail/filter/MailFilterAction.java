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
package org.columba.mail.filter;

import org.columba.core.filter.FilterAction;
import org.columba.core.filter.IFilterAction;
import org.columba.core.xml.XmlElement;

/**
 * @author fdietz
 *  
 */
public class MailFilterAction extends FilterAction {

	/**
	 * @param root
	 */
	public MailFilterAction(XmlElement root) {
		super(root);
	}
	
	public MailFilterAction(IFilterAction parent) {
		this(parent.getRoot());
	}

	/**
	 * Mark message variant can be of value: <br>
	 * <ul>
	 * <li>read</li>
	 * <li>unread</li>
	 * <li>flagged</li>
	 * <li>not_flagged</li>
	 * <li>expunged</li>
	 * <li>not_expunged</li>
	 * <li>answered</li>
	 * <li>spam</li>
	 * <li>no_spam</li>
	 * </ul>
	 * 
	 * @param s
	 */
	public void setMarkVariant(String s) {
		setString("markvariant", s);
	}

	public String getMarkVariant() {
		return get("markvariant");
	}

}