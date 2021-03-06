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

package org.frapuccino.checkabletree;

import javax.swing.JTree;
import javax.swing.BorderFactory;

/**
 * A tree with items that can be checked or unchecked. This is
 * useful if you want to let the user select items from a hierarchical
 * realm.
 *
 * @author fdietz
 */
public class CheckableTree extends JTree {
    
    /**
     * Creates a new checkable tree using a dummy root node.
     */
    public CheckableTree() {
        this(new CheckableItemImpl("root"));
    }
    
    /**
     * Creates a new checkable tree using the given root node.
     */
    public CheckableTree(CheckableItem root) {
        super(root);
        setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        setCellRenderer(new CheckRenderer());
    }
}
