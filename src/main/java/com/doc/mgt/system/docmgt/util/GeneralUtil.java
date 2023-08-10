package com.doc.mgt.system.docmgt.util;



import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtil {

    public static void validateUsernameAndEmail(String username, String email) {
        // check that first name is not null or empty
        if (GeneralUtil.stringIsNullOrEmpty(username)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "username cannot be null or empty!");
        }

        // check that last name is not null or empty
        if (GeneralUtil.stringIsNullOrEmpty(email)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "email cannot be null or empty!");
        }

    }

    public static void verifyStringFieldIsPresent(String value, String fieldName, int rowNo) {
        boolean notValid = stringIsNullOrEmpty(value);
        if (notValid) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode,
                    fieldName + " on row " + rowNo + " cannot be empty");
        }
    }

//    public static boolean invalidEmail(String email) {
//        if (stringIsNullOrEmpty(email)) return true;
//
//        return !EmailValidator.getInstance().isValid(email);
//    }

    public static boolean stringIsNullOrEmpty(String arg) {
        if ((arg == null)) return true;
        else
            return ("".equals(arg)) || (arg.trim().length() == 0);
    }

    public static boolean containSpecialCharacter(String matchingString) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(matchingString);
        return m.find();
    }


}
