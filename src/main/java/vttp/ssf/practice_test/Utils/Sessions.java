package vttp.ssf.practice_test.Utils;

import jakarta.servlet.http.HttpSession;

public class Sessions {

    public static void createSession(HttpSession session, String value) {
        session.setAttribute("loggedInUser", value);
    }

    public static boolean checkSession(HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        return loggedInUser != null;
    }

}
