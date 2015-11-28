package com.societegenerale;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SGController {
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE })
    public @ResponseBody String numMembers() {
        int count = DAO.getCount();
        String response;
        if (count == -1) {
            response = "{\"message\": \"Error processing your request. Please try again later.\"}";
        } else {
            response = "{\"count\":" + count + "}";
        }
        return response;
    }

    // Times from
    // http://www.learnersdictionary.com/qa/parts-of-the-day-early-morning-late-morning-etc
    @RequestMapping(value = "/greetings", method = RequestMethod.GET)
    public @ResponseBody String greeting() {
        String response = "Good ";
        LocalDateTime time = LocalDateTime.now();
        int hour = time.getHour();
        if (hour >= 5 && hour < 12) {
            response += " morning!";
        } else if (hour >= 12 && hour < 5) {
            response += " afternoon!";
        } else if (hour >= 5 && hour < 9) {
            response += " evening!";
        } else {
            response += " evening!"; // " late evening!"; // " night!";
        }
        return response;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE })
    public @ResponseBody String search(@RequestParam(required = true) String query) {
        List<Member> members = null;
        try {
            query = URLDecoder.decode(query.trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "{\"message\":\"Error processing your request. Please try again later.\"}";
        }

        if (query.isEmpty() || query.startsWith("query=")) {
            members = DAO.getMembers(query);
        } else {
            String[] parts = query.split("&");
            String[][] finalParts = new String[parts.length][2];
            List<String> columns = new ArrayList<>();
            columns.add("id");
            columns.add("status");
            columns.add("race");
            columns.add("weight");
            columns.add("height");
            columns.add("is_veg");
            List<String> badParts = new ArrayList<>();
            for (int i = 0; i < parts.length; i++) {
                String[] subpart = parts[i].split("=");
                if (!columns.contains(subpart[0])) {
                    badParts.add(subpart[0]);
                } else {
                    finalParts[i][0] = subpart[0];
                    finalParts[i][1] = subpart[1];
                }
            }
            if (badParts.size() > 0) {
                return "{\"message\":\"We don't recognize the following parameters: "
                        + badParts.toString() + ".\"}";
            }
            members = DAO.getMembers(finalParts);
        }

        if (members == null) {
            return "{\"message\":\"Error processing your request. Please try again later.\"}";
        } else {
            return "{\"result\":\"" + members.toString() + "\"}";
        }
    }
}
