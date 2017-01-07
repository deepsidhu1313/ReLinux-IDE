/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.PlainTextChange;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.reactfx.EventStream;
import org.reactfx.util.Try;

/**
 *
 * @author Nika
 */
public class SyntaxTextArea {

    static String DECLARATIONS[] = new String[]{"class", "enum",
        "extends", "implements",
        "instanceof", "interface",
        "native", "throws"};
    static String PRIMITIVES[] = new String[]{
        "boolean", "byte", "char", "double",
        "float", "int", "long", "short",
        "void"};
    static String EXTERNALS[] = new String[]{
        "import", "package"};
    static String STORAGECLASS[] = new String[]{
        "abstract", "final", "static", "strictfp",
        "synchronized", "transient", "volatile"};
    static String SCOPEDECLARATIONS[] = new String[]{
        "private", "protected", "public"};
    static String FLOW[] = new String[]{
        "assert", "break", "case", "catch", "continue", "default", "do", "else",
        "finally", "for", "if", "return", "throw", "switch", "try", "while"};
    static String MEMORY[] = new String[]{
        "new", "super", "this"};
    static String FUTURE[] = new String[]{
        "const", "goto"};
    static String NULL[] = new String[]{
        "null"};
    static String BOOLEAN[] = new String[]{"true", "false"};

    static String DECLARATIONS_PATTERN = "\\b(" + String.join("|", DECLARATIONS) + ")\\b";
    static String PRIMITIVES_PATTERN = "\\b(" + String.join("|", PRIMITIVES) + ")\\b";
    static String EXTERNALS_PATTERN = "\\b(" + String.join("|", EXTERNALS) + ")\\b";
    static String STORAGECLASS_PATTERN = "\\b(" + String.join("|", STORAGECLASS) + ")\\b";
    static String SCOPEDECLARATIONS_PATTERN = "\\b(" + String.join("|", SCOPEDECLARATIONS) + ")\\b";
    static String FLOW_PATTERN = "\\b(" + String.join("|", FLOW) + ")\\b";
    static String MEMORY_PATTERN = "\\b(" + String.join("|", MEMORY) + ")\\b";
    static String FUTURE_PATTERN = "\\b(" + String.join("|", FUTURE) + ")\\b";
    static String NULL_PATTERN = "\\b(" + String.join("|", NULL) + ")\\b";
    static String BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<DECLARATIONS>" + DECLARATIONS_PATTERN + ")"
            + "|(?<PRIMITIVES>" + PRIMITIVES_PATTERN + ")"
            + "|(?<EXTERNALS>" + EXTERNALS_PATTERN + ")"
            + "|(?<STORAGECLASS>" + STORAGECLASS_PATTERN + ")"
            + "|(?<SCOPEDECLARATIONS>" + SCOPEDECLARATIONS_PATTERN + ")"
            + "|(?<FLOW>" + FLOW_PATTERN + ")"
            + "|(?<MEMORY>" + MEMORY_PATTERN + ")"
            + "|(?<FUTURE>" + FUTURE_PATTERN + ")"
            + "|(?<NULL>" + NULL_PATTERN + ")"
            + "|(?<BOOLEAN>" + BOOLEAN_PATTERN + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private CodeArea codeArea;
    private ExecutorService executor;

    public Scene scene;

    public SyntaxTextArea() {

        executor = Executors.newSingleThreadExecutor();
        codeArea = new CodeArea();

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        EventStream<PlainTextChange> textChanges = codeArea.plainTextChanges();
        textChanges
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(textChanges)
                .map(Try::get)
                .subscribe(this::applyHighlighting);
        codeArea.getStylesheets().add(SyntaxTextArea.class.getResource("res/css/default/java.css").toExternalForm());
    }

    public void setText(String text) {
        codeArea.replaceText(0, 0, text);
        codeArea.getUndoManager().forgetHistory();
        codeArea.getUndoManager().mark();

    }

    public String getText() {
        return codeArea.getText();

    }

    public void appendText(String text) {
        codeArea.appendText(text);

    }

    public void setFont(Font v) {
        codeArea.setFont(v);
    }

    public CodeArea getNode() {
        return codeArea;
    }

    public void setStyling() {

    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private  StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = styleClass = matcher.group("DECLARATIONS") != null ? "declarations"
                    : matcher.group("PRIMITIVES") != null ? "primitives"
                    : matcher.group("EXTERNALS") != null ? "externals"
                    : matcher.group("STORAGECLASS") != null ? "storageclass"
                    : matcher.group("SCOPEDECLARATIONS") != null ? "scopedeclarations"
                    : matcher.group("FLOW") != null ? "flow"
                    : matcher.group("MEMORY") != null ? "memory"
                    : matcher.group("FUTURE") != null ? "future"
                    : matcher.group("NULL") != null ? "nullvalue"
                    : matcher.group("BOOLEAN") != null ? "boolean"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("COMMENT") != null ? "comment"
                    : null;
            /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
