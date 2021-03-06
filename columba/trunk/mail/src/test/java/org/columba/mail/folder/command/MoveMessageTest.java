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

package org.columba.mail.folder.command;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.columba.core.command.NullWorkerStatusController;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.AbstractFolderTst;
import org.columba.mail.folder.FolderTstHelper;
import org.columba.mail.folder.IMailboxInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fdietz
 */
public class MoveMessageTest extends AbstractFolderTst {
    
    /**
     * @param arg0
     */
    public MoveMessageTest(Class factory) {
        super(factory);
    }

    @Test
    public void testMoveMessage() throws Exception {
        //		 add message "0.eml" as inputstream to folder
        String input = FolderTstHelper.getString(0);

        // create stream from string
        ByteArrayInputStream inputStream = FolderTstHelper.getByteArrayInputStream(input);
        // add stream to folder
        Object uid = getSourceFolder().addMessage(inputStream);

//      create Command reference
        MailFolderCommandReference ref = new MailFolderCommandReference(
				getSourceFolder(), getDestFolder(), new Object[] { uid });

        // create copy command
        MoveMessageCommand command = new MoveMessageCommand(ref);

        // execute command -> use mock object class as worker which does nothing
        command.execute(NullWorkerStatusController.getInstance());

        // get inputstream of this message from folder
        InputStream outputStream = destFolder.getMessageSourceStream(uid);
        // create string from inputstream
        String output = FolderTstHelper.getStringFromInputStream(outputStream);
        // compare both messages
        Assert.assertEquals(input, output);
        IMailboxInfo info = getDestFolder().getMessageFolderInfo();
        Assert.assertEquals("one message should be in destination folder", 1, info.getExists());
        info = getSourceFolder().getMessageFolderInfo();
        // close streams
        inputStream.close();
        outputStream.close();
    }
}
