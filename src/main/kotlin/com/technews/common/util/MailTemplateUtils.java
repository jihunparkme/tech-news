package com.technews.common.util;

import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.subscribe.dto.SubscriberMailContents;

@Deprecated
public class MailTemplateUtils {

    public static String generateContents(final SubscriberMailContents subscriberMailContents) {
        StringBuffer sb = new StringBuffer();
        sb.append("      <h3 style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-style:italic;font-weight:blod\">\n" +
                "        Spring\n" +
                "      </h3>\n" +
                "      <h4 style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-style:italic;font-weight:500\">\n" +
                "          Release\n" +
                "      </h4><ul>\n");
        for (final Release springRelease : subscriberMailContents.getSpringReleases()) {
            sb.append("            <li><a\n" +
                    "                style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-size: 15px; margin-left: auto; margin-right: auto; text-align: justify;color: #666;line-height:1.5;\"\n" +
                    "                href=\"");
            sb.append(springRelease.getUrl());
            sb.append("\" target=\"_blank\">\n");
            sb.append(springRelease.getProject() + " : " + springRelease.getVersion());
            sb.append("\n      </a><br/></li>\n");
        }
        sb.append("      </ul>\n");

        sb.append("      <h4 style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-style:italic;font-weight:500\">\n" +
                "          Blog\n" +
                "      </h4><ul>\n");
        for (final Post springPost : subscriberMailContents.getSpringPosts()) {
            sb.append("            <li><a\n" +
                    "                style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-size: 15px; margin-left: auto; margin-right: auto; text-align: justify;color: #666;line-height:1.5;margin-bottom:75px\"\n" +
                    "                href=\"");
            sb.append(springPost.getUrl());
            sb.append("\" target=\"_blank\">\n");
            sb.append(springPost.getTitle());
            sb.append("\n      </a><br/></li>\n");
        }
        sb.append("      </ul>\n");

        sb.append("<h3 style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-style:italic;font-weight:blod;margin-top: 3em;\">\n" +
                "          Java\n" +
                "      </h3>\n" +
                "      <h4 style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-style:italic;font-weight:500\">\n" +
                "          Release\n" +
                "      </h4><ul>\n");
        for (final Release javaRelease : subscriberMailContents.getJavaReleases()) {
            sb.append("            <li><a\n" +
                    "                style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-size: 15px; margin-left: auto; margin-right: auto; text-align: justify;color: #666;line-height:1.5;\"\n" +
                    "                href=\"");
            sb.append(javaRelease.getUrl());
            sb.append("\" target=\"_blank\">\n");
            sb.append(javaRelease.getProject() + " : " + javaRelease.getVersion());
            sb.append("\n      </a><br/></li>\n");
        }
        sb.append("      </ul>\n");

        sb.append("      <h4 style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-style:italic;font-weight:500\">\n" +
                "          Blog\n" +
                "      </h4><ul>\n");
        for (final Post javaPost : subscriberMailContents.getJavaPosts()) {
            sb.append("            <li><a\n" +
                    "                style=\"font-family:Palatino Linotype, Book Antiqua, Palatino, serif;font-size: 15px; margin-left: auto; margin-right: auto; text-align: justify;color: #666;line-height:1.5;margin-bottom:75px;\"\n" +
                    "                href=\"");
            sb.append(javaPost.getUrl());
            sb.append("\" target=\"_blank\">\n");
            sb.append(javaPost.getTitle());
            sb.append("\n      </a><br/></li>\n");
        }
        sb.append("      </ul>\n");

        sb.append("      <p style=\"margin-bottom:75px;\"></p>");

        return mergeTemplates(sb.toString());
    }

    private static String getPrefix() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<body>\n" +
                "  <style>\n" +
                "      body {\n" +
                "          background-color: #EEEEEE;\n" +
                "      }\n" +
                "  </style>\n" +
                "  <div style=\"max-width:550px; min-width:320px;  background-color: white; border: 1px solid #DDDDDD; margin-right: auto; margin-left: auto;\">\n" +
                "    <div style=\"margin-left:30px;margin-right:30px\">\n" +
                "      <p>&nbsp;</p>\n" +
                "      <p style=\"text-decoration:none; font-family:Verdana, Geneva, sans-serif; font-weight: bold; color: #3D3D3D; font-size: 15px\">Tech News</p>\n" +
                "      <hr style=\"margin-top:10px;margin-bottom:40px;border:none;border-bottom:1px solid red\">\n" +
                "      <h1\n" +
                "        style=\"font-family: Tahoma, Geneva, sans-serif; font-weight: normal; color: #2A2A2A; text-align: center; margin-bottom: 65px;font-size: 20px; letter-spacing: 6px;font-weight: normal; border: 2px solid black; padding: 15px;\">\n" +
                "        Java & Spring Tech News</h1>");

        return sb.toString();
    }

    private static String getPostfix() {
        final StringBuffer sb = new StringBuffer();
        sb.append("      <table style=\"width:100%;\">\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <th></th>\n" +
                "            <td style=\"width:25%\"></td>\n" +
                "            <td style=\"background-color:black; with:50%; text-align:center; padding:15px\">\n" +
                "                <a href=\"http://localhost:8080/\" style=\"margin-left: auto; margin-right: auto;text-decoration:none;color: white;text-align:center;font-family:Courier New, Courier, monospace;font-weight:600;letter-spacing:2px;background-color:black;padding:15px\">\n" +
                "                Homepage</a></td>\n" +
                "            <td style=\"width:25%\"></td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "      <hr style=\"margin-top:10px; margin-top:75px\">\n" +
                "      <p style=\"text-align:center; margin-bottom:15px\">\n" +
                "        <small style=\"text-align:center; font-family:Courier New, Courier, monospace;font-size:10px; color#666;\">\n" +
                "            You have received the email because you are subscribing to Tech News.<br/>\n" +
                "            If you want to unsubscribe, you can request it on homepage.\n" +
                "      <p>\n" +
                "        &nbsp;\n" +
                "      </p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>");

        return sb.toString();
    }

    private static String mergeTemplates(String contents) {
        return getPrefix() + contents + getPostfix();
    }
}
