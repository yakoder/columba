/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Ristretto Mail API.
 *
 * The Initial Developers of the Original Code are
 * Timo Stich and Frederik Dietz.
 * Portions created by the Initial Developers are Copyright (C) 2004
 * All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.columba.ristretto.imap.parser;

import org.columba.ristretto.imap.ResponseTextCode;
import org.columba.ristretto.parser.ParserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for ResponseTextCodes.
 * 
 * @author tstich
 *
 */
public class ResponseTextCodeParser {
    
    private static final Pattern typePattern = Pattern.compile("^((ALERT)|" +
    		"(BADCHARSET)|" +	
    		"(CAPABILITY)|" +	
    		"(PARSE)|" +
    		"(PERMANENTFLAGS)|" +
    		"(READ.ONLY)|" +
    		"(READ.WRITE)|" +
    		"(TRYCREATE)|" +
    		"(UIDNEXT)|" +
    		"(UIDVALIDITY)|" +
    		"(UNSEEN))\\s?(.*)");	// parameters go in # 11
    
    /**
     * Parse the ResponsesTextcode of the IMAP response.
     * 
     * @param in
     * @return the ResponseTextCode
     * @throws ParserException
     */
    public static ResponseTextCode parse(String in) throws ParserException {
        ResponseTextCode result = new ResponseTextCode();
        Matcher matcher = typePattern.matcher(in);
        
        if( matcher.find() ) {
            if( matcher.group(2) != null ) {
                result.setType(ResponseTextCode.ALERT);
            } else if( matcher.group(3) != null ) {
                result.setType(ResponseTextCode.BADCHARSET);
                result.setStringArrayValue(StringListParser.parse(matcher.group(13)));
            } else if( matcher.group(4) != null ) {
                result.setType(ResponseTextCode.CAPABILITY);
                result.setStringArrayValue(StringListParser.parse(matcher.group(13)));
            } else if( matcher.group(5)!=null) {
                result.setType(ResponseTextCode.PARSE);
            } else if( matcher.group(6)!=null) {
                result.setType(ResponseTextCode.PERMANENTFLAGS);
                result.setStringArrayValue(StringListParser.parse(matcher.group(13)));
            } else if( matcher.group(7)!=null) {
                result.setType(ResponseTextCode.READ_ONLY);
            } else if( matcher.group(8)!=null) {
                result.setType(ResponseTextCode.READ_WRITE);
            } else if( matcher.group(9)!=null) {
                result.setType(ResponseTextCode.TRYCREATE);
            } else if( matcher.group(10)!=null) {
                result.setType(ResponseTextCode.UIDNEXT);
                result.setIntValue(parseIntValue(matcher.group(13)));
            } else if( matcher.group(11)!=null) {
                result.setType(ResponseTextCode.UIDVALIDITY);
                result.setIntValue(parseIntValue(matcher.group(13)));
            } else if( matcher.group(12)!=null) {
                result.setType(ResponseTextCode.UNSEEN);
                result.setIntValue(parseIntValue(matcher.group(13)));
            }
        } else {
            result.setType(ResponseTextCode.UNSPECIFIED);
            result.setStringValue(in);
        }
        
        return result;
    }

    private static int parseIntValue(String string) {
        return Integer.parseInt(string);
    }

    
}
