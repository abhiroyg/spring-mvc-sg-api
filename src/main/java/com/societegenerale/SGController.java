package com.societegenerale;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @RequestMapping(value = { "/", "/greetings" }, method = RequestMethod.GET)
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
    public @ResponseBody String search(
            @RequestParam Map<String, String> requestParams) {
        System.out.println(requestParams);

        if (requestParams.containsKey("query")) {
            if (requestParams.size() > 1) {
                return "{\"message\":\"Does not support multiple parameters along with 'query'.\"}";
            }
            requestParams.put("status", requestParams.get("query"));
            requestParams.remove("query");
        }

        List<String> columns = new ArrayList<>();
        columns.add("id");
        columns.add("status");
        columns.add("race");
        columns.add("weight");
        columns.add("height");
        columns.add("is_veg");
        Set<String> keys = requestParams.keySet();
        if (!columns.containsAll(keys)) {
            keys.removeAll(columns);
            return "{\"message\":\"We don't recognize the following parameters: "
                    + keys.toString() + ".\"}";
        }

        List<Member> members = DAO.getMembers(requestParams);

        if (members == null) {
            return "{\"message\":\"Error processing your request. Please try again later.\"}";
        } else {
            return "{\"count\":" + members.size() + ",\"result\":\""
                    + members.toString() + "\"}";
        }
    }
}
