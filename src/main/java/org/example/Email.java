package org.example;

import java.io.File;
import java.util.List;

public record Email(String from, String to, String subject, String text, String category, List<File> attachments) {
}
