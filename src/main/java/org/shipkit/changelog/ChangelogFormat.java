package org.shipkit.changelog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Formats the changelog content.
 */
public class ChangelogFormat {

    /**
     * Builds the changelog String based on the input parameters.
     */
    public static String formatChangelog(Collection<String> contributors, Collection<Ticket> tickets, int commitCount,
                                         final String version, final String previousRev,
                                         final String gitHubRepoUrl, final String date) {
        String template = "@header@\n" +
                "\n" +
                "#### @version@\n" +
                " - @date@ - [@commitCount@ commit(s)](@repoUrl@/compare/@previousRev@...@newRev@) by @contributors@\n" +
                "@improvements@";

        Map<String, String> data = new HashMap<String, String>() {{
            put("header", "<sup><sup>*Changelog generated by [Shipkit Changelog Gradle Plugin](https://github.com/shipkit/shipkit-changelog)*</sup></sup>");
            put("version", version);
            put("date", date);
            put("commitCount", "" + commitCount);
            put("repoUrl", gitHubRepoUrl);
            put("previousRev", previousRev);
            put("newRev", "v" + version);
            put("contributors", String.join(", ", contributors));
            put("improvements", formatImprovements(tickets));
        }};

        return replaceTokens(template, data);
    }

    private static String formatImprovements(Collection<Ticket> tickets) {
        if (tickets.isEmpty()) {
            return " - No notable improvements. No pull requests (issues) were referenced from commits.";
        }
        return String.join("\n", tickets.stream().map(i -> " - " + i.getTitle() +
                " [(#" + i.getId() + ")](" +
                i.getUrl() + ")").collect(Collectors.toList()));
    }

    private static String replaceTokens(String text,
                                       Map<String, String> replacements) {
        Pattern pattern = Pattern.compile("@(.+?)@");
        Matcher matcher = pattern.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            if (replacement != null) {
                matcher.appendReplacement(buffer, "");
                buffer.append(replacement);
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
