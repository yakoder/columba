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
package org.columba.mail.folder;

import java.io.ByteArrayInputStream;

import org.columba.mail.folder.command.MarkMessageCommand;
import org.columba.ristretto.message.Flags;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fdietz
 *  
 */
public class ExpungeFolderTest extends AbstractFolderTst {
    
    /**
     * @param arg0
     */
    public ExpungeFolderTest(Class factory) {
        super(factory);

    }

    /**
     * Expunge folder, message is *not* marked as expunged
     * 
     * @throws Exception
     */
    @Test
    public void testExpungeMessage() throws Exception {
        //		 add message "0.eml" as inputstream to folder
        String input = FolderTstHelper.getString(0);

        // create stream from string
        ByteArrayInputStream inputStream = FolderTstHelper
                .getByteArrayInputStream(input);
        // add stream to folder
        Object uid = getSourceFolder().addMessage(inputStream);
        //      get flags of message
        Flags oldFlags = getSourceFolder().getFlags(uid);
        // set flags
        oldFlags.setSeen(false);
        oldFlags.setRecent(true);
        oldFlags.setFlagged(true);
        oldFlags.setDeleted(false);

        getSourceFolder().expungeFolder();

        Object[] uids = getSourceFolder().getUids();
        Assert.assertEquals("one message should be in source folder", 1, uids.length);
        IMailboxInfo info = getSourceFolder().getMessageFolderInfo();
        Assert.assertEquals("one message should be in source folder", 1, info
                .getExists());
        // close streams
        inputStream.close();
    }

    /**
     * Expunge folder, one message is marked as expunged
     * 
     * @throws Exception
     */
    @Test
    public void testExpungeMessage2() throws Exception {
        //		 add message "0.eml" as inputstream to folder
        String input = FolderTstHelper.getString(0);

        // create stream from string
        ByteArrayInputStream inputStream = FolderTstHelper
                .getByteArrayInputStream(input);
        // add stream to folder
        Object uid = getSourceFolder().addMessage(inputStream);

        getSourceFolder().markMessage(new Object[] {uid}, MarkMessageCommand.MARK_AS_EXPUNGED);
        
        getSourceFolder().expungeFolder();

        Object[] uids = getSourceFolder().getUids();
        Assert.assertEquals("null message should be in source folder", 0, uids.length);
        IMailboxInfo info = getSourceFolder().getMessageFolderInfo();
        Assert.assertEquals("null message should be in source folder", 0, info
                .getExists());
        // close streams
        inputStream.close();
    }
}
