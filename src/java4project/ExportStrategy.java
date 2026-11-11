package java4project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JTable;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public interface ExportStrategy {

    void export(JTable table) throws Exception;
}

class ExportCsv implements ExportStrategy {

    @Override
    public void export(JTable table) throws Exception {
        TableModel model = table.getModel();
        String filePath = "data/medias.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                writer.write(model.getColumnName(j));
                // add , pour chque colone
                if (j < model.getColumnCount() - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    String data = (value != null) ? value.toString() : "";

                    // Protection si la donnée contient une virgule, on l'entoure de guillemets
                    if (data.contains(",")) {
                        data = "\"" + data + "\"";
                    }

                    writer.write(data);

                    if (j < model.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine(); 
            }
        }
    }
}

class ExportXml implements ExportStrategy {

    @Override
    public void export(JTable table) throws Exception {
        TableModel model = table.getModel();

        Element root = new Element("medias");
        Document document = new Document(root);

        for (int i = 0; i < model.getRowCount(); i++) {
            Element mediaElement = new Element("media");

            for (int j = 0; j < model.getColumnCount(); j++) {
                String columnName = model.getColumnName(j).replaceAll("\\s+", "_"); 
                Object value = model.getValueAt(i, j);

                // <Titre>Java</Titre>
                Element info = new Element(columnName);
                info.setText(value != null ? value.toString() : "");
                mediaElement.addContent(info);
            }

            root.addContent(mediaElement);
        }

        //Save the document to the file
        XMLOutputter xmlOutput = new XMLOutputter();

        try (FileOutputStream out = new FileOutputStream("data/media.xml")) {
            xmlOutput.output(document, out);
        }

        System.out.println("Export JDOM réussi !");
    }
}

class ExporterContext {

    private ExportStrategy strategy;

    public void setStrategy(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public void exportData(JTable table, String filePath) throws Exception {
        if (strategy != null) {
            strategy.export(table);
        } else {
            throw new IllegalStateException("Aucune stratégie d'export définie !");
        }
    }
}
