package theseshelper.review;

import java.io.*;

import org.testng.*;
import org.testng.annotations.*;

public class ReviewWriterTest {

    @Test
    public void writeTest() throws IOException {
        final File reviewFile = File.createTempFile("review", ".json");
        reviewFile.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reviewFile))) {
            writer.write("{");
            writer.write("\"type\": \"BA\",");
            writer.write("\"title\": \"Toller Titel\",");
            writer.write("\"student\": \"Hans Dampf\",");
            writer.write("\"date\": \"1.~April~2000\",");
            writer.write("\"place\": \"Giambola\",");
            writer.write("\"reviewer\": \"\\\\prof{Peter Pauker}\",");
            writer.write("\"signature\": \"../pictures/signature.png\",");
            writer.write("\"restricted\": true,");
            writer.write("\"tworeviewers\": false,");
            writer.write("\"otherreviewer\": \"\\\\prof{Willi Wichtig}\",");
            writer.write("\"goal\": \"Die Arbeit verfolgt die Forschungsfrage, ob Pinguine fliegen können.\",");
            writer.write("\"contributions\": [\"Experiment\", \"Interview\"],");
            writer.write("\"evaluationgroups\": [],");
            writer.write("\"criteriapath\": \"criteria.json\",");
            writer.write("\"bonusstart\": \"War toll, daher Bonus.\",");
            writer.write("\"bonus\": \"1\",");
            writer.write("\"totalstart\": \"Nu is gut.\",");
            writer.write("\"additionaltotaltext\": \"Schlusswort.\",");
            writer.write("\"totalexpected\": \"100\"");
            writer.write("}");
        }
        final File criteria = reviewFile.toPath().toAbsolutePath().getParent().resolve("criteria.json").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(criteria))) {
            writer.write("{}");
        }
        criteria.deleteOnExit();
        final File result = ReviewWriter.write(reviewFile);
        result.deleteOnExit();
        try (BufferedReader reader = new BufferedReader(new FileReader(result))) {
            Assert.assertEquals(reader.readLine(), "\\documentclass{article}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\usepackage{fhdwutil}");
            Assert.assertEquals(reader.readLine(), "\\usepackage{fhdwevaluation}");
            Assert.assertEquals(reader.readLine(), "\\usepackage[a4paper,margin=2.5cm]{geometry}");
            Assert.assertEquals(reader.readLine(), "\\usepackage{setspace}");
            Assert.assertEquals(reader.readLine(), "\\usepackage{graphicx}");
            Assert.assertEquals(reader.readLine(), "\\usepackage{xcolor}");
            Assert.assertEquals(reader.readLine(), "\\usepackage{adjustbox}");
            Assert.assertEquals(reader.readLine(), "\\usepackage{tikz}");
            Assert.assertEquals(
                reader.readLine(),
                "\\usetikzlibrary{arrows,shapes,chains,matrix,positioning,scopes,decorations.pathmorphing,"
                + "decorations.pathreplacing,shadows,calc,trees}"
            );
            Assert.assertEquals(reader.readLine(), "\\usepackage{tkz-kiviat}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\begin{document}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\begin{center}");
            Assert.assertEquals(reader.readLine(), "{\\Huge \\textbf{Gutachten}}\\\\[5ex]");
            Assert.assertEquals(
                reader.readLine(),
                "{\\large über die Bachelorarbeit von \\textbf{Hans Dampf} mit dem Titel}\\\\[2ex]"
            );
            Assert.assertEquals(reader.readLine(), "\\begin{spacing}{1.8}");
            Assert.assertEquals(reader.readLine(), "{\\LARGE \\textbf{Toller Titel}}");
            Assert.assertEquals(reader.readLine(), "\\end{spacing}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\vspace*{1ex}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "{\\large \\prof{Peter Pauker}\\\\[2ex] Datum: 1.~April~2000}");
            Assert.assertEquals(reader.readLine(), "\\end{center}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\vspace*{2ex}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\begin{center}");
            Assert.assertEquals(
                reader.readLine(),
                "\\textcolor{red}{Die Arbeit enthält einen Sperrvermerk, sodass auch dieses "
                + "Gutachten\\\\entsprechend vertraulich behandelt werden muss.}"
            );
            Assert.assertEquals(reader.readLine(), "\\end{center}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\vspace*{2ex}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\section{Inhalt}");
            Assert.assertEquals(
                reader.readLine(),
                "Die Arbeit verfolgt die Forschungsfrage, ob Pinguine fliegen können.\\\\"
            );
            Assert.assertEquals(reader.readLine(), "Dazu werden die folgenden Beiträge erbracht:");
            Assert.assertEquals(reader.readLine(), "\\begin{itemize}");
            Assert.assertEquals(reader.readLine(), "\\item Experiment");
            Assert.assertEquals(reader.readLine(), "\\item Interview");
            Assert.assertEquals(reader.readLine(), "\\end{itemize}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\section{Gesamtbeurteilung}");
            Assert.assertEquals(reader.readLine(), "War toll, daher Bonus.");
            Assert.assertEquals(reader.readLine(), "Es wurde 1 Bonuspunkt gewährt.");
            Assert.assertEquals(reader.readLine(), "Insgesamt wurde 1 Punkt erreicht und das Gesamturteil lautet:");
            Assert.assertEquals(reader.readLine(), "\\begin{center}{\\large\\textbf{5{,}0}}\\end{center}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\vspace*{5ex}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\begin{flushright}");
            Assert.assertEquals(reader.readLine(), "Giambola, den 1.~April~2000");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\vspace*{4ex}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(
                reader.readLine(),
                "\\includegraphics[height=1.5cm]{../pictures/signature.png}\\hspace*{-10mm}"
            );
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\begin{minipage}{5cm}");
            Assert.assertEquals(reader.readLine(), "\\hrulefill");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\vspace*{-2ex}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\begin{center}");
            Assert.assertEquals(reader.readLine(), "\\prof{Peter Pauker}");
            Assert.assertEquals(reader.readLine(), "\\end{center}");
            Assert.assertEquals(reader.readLine(), "\\end{minipage}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\end{flushright}");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\vfill");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(
                reader.readLine(),
                "\\noindent Auf den nachfolgenden Seiten wird diese Bewertung näher erläutert."
            );
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\pagebreak");
            Assert.assertEquals(reader.readLine(), "");
            Assert.assertEquals(reader.readLine(), "\\end{document}");
            Assert.assertEquals(reader.readLine(), null);
        }
    }

}
