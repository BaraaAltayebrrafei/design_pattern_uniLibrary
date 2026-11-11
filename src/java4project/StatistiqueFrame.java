package java4project;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatistiqueFrame extends JFrame {

    // Controls
    private JComboBox<String> comboSpec; // Selected Specialite
    private JComboBox<String> comboMat;  // Selected Matiere
    
    // Charts
    private SimplePieChart pieChart;
    private SimpleColumnChart columnChart;

    public StatistiqueFrame() {
        setTitle("Statistiques Avancées");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ==========================================
        // 1. TOP PANEL (Controls)
        // ==========================================
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // -- Left Control (For Pie Chart - Specialite) --
        JPanel pnlLeftControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLeftControl.add(new JLabel("Spécialité:"));
        comboSpec = new JComboBox<>(new String[]{"Informatique", "Droit", "Gestion"});
        pnlLeftControl.add(comboSpec);
        
        // -- Right Control (For Column Chart - Matiere) --
        JPanel pnlRightControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlRightControl.add(new JLabel("Matière:"));
        comboMat = new JComboBox<>(new String[]{"Java", "Base de Données", "Web Design"});
        pnlRightControl.add(comboMat);

        topPanel.add(pnlLeftControl);
        topPanel.add(pnlRightControl);
        add(topPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. CENTER PANEL (Charts)
        // ==========================================
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // -- Chart A: Pie Chart (Left) --
        pieChart = new SimplePieChart();
        pieChart.setBorder(BorderFactory.createTitledBorder("Répartition des Médias (Par Spécialité)"));
        chartsPanel.add(pieChart);

        // -- Chart B: Column Chart (Right) --
        columnChart = new SimpleColumnChart();
        columnChart.setBorder(BorderFactory.createTitledBorder("Moyenne des Notes (Par Matière)"));
        chartsPanel.add(columnChart);

        add(chartsPanel, BorderLayout.CENTER);

        // ==========================================
        // 3. EVENTS
        // ==========================================
        
        // Listener for Specialite (Updates Pie Chart)
        comboSpec.addActionListener(e -> updatePieChart());

        // Listener for Matiere (Updates Column Chart)
        comboMat.addActionListener(e -> updateColumnChart());

        // Initial Load
        updatePieChart();
        updateColumnChart();

        setVisible(true);
    }

    // --- Logic to update Pie Chart data based on Specialite ---
    private void updatePieChart() {
        String specialite = (String) comboSpec.getSelectedItem();
        pieChart.clearData();

        // Dummy Data: Different specialties have different media breakdowns
        if ("Informatique".equals(specialite)) {
            pieChart.addSlice("Video", 60);
            pieChart.addSlice("Docs", 20);
            pieChart.addSlice("Quiz", 20);
        } else if ("Droit".equals(specialite)) {
            pieChart.addSlice("Video", 10);
            pieChart.addSlice("Docs", 80);
            pieChart.addSlice("Quiz", 10);
        } else {
            // Gestion
            pieChart.addSlice("Video", 30);
            pieChart.addSlice("Docs", 40);
            pieChart.addSlice("Quiz", 30);
        }
        pieChart.repaint();
    }

    // --- Logic to update Column Chart data based on Matiere ---
    private void updateColumnChart() {
        String matiere = (String) comboMat.getSelectedItem();
        columnChart.clearData();

        // Dummy Data: Average grades or student count per group
        if ("Java".equals(matiere)) {
            columnChart.addBar("Groupe A", 15);
            columnChart.addBar("Groupe B", 12);
            columnChart.addBar("Groupe C", 18);
        } else if ("Base de Données".equals(matiere)) {
            columnChart.addBar("Groupe A", 10);
            columnChart.addBar("Groupe B", 14);
            columnChart.addBar("Groupe C", 11);
        } else {
            columnChart.addBar("Groupe A", 19);
            columnChart.addBar("Groupe B", 16);
            columnChart.addBar("Groupe C", 15);
        }
        columnChart.repaint();
    }

    // ==========================================
    // COMPONENT 1: PIE CHART (For Specialite)
    // ==========================================
    class SimplePieChart extends JPanel {
        private Map<String, Double> slices = new LinkedHashMap<>();
        private Color[] colors = {new Color(65, 105, 225), new Color(255, 140, 0), new Color(34, 139, 34)};

        public void addSlice(String name, double value) { slices.put(name, value); }
        public void clearData() { slices.clear(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (slices.isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double total = slices.values().stream().mapToDouble(Double::doubleValue).sum();
            int startAngle = 0;
            int i = 0;

            int size = Math.min(getWidth(), getHeight()) - 80;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2 - 10;

            for (Map.Entry<String, Double> entry : slices.entrySet()) {
                int arcAngle = (int) (entry.getValue() * 360 / total);
                g2.setColor(colors[i % colors.length]);
                g2.fillArc(x, y, size, size, startAngle, arcAngle);
                
                // Legend
                g2.fillRect(10, getHeight() - 50 + (i * 15), 10, 10);
                g2.setColor(Color.BLACK);
                g2.drawString(entry.getKey() + " (" + (int)entry.getValue().doubleValue() + "%)", 25, getHeight() - 40 + (i * 15));

                startAngle += arcAngle;
                i++;
            }
        }
    }

    // ==========================================
    // COMPONENT 2: COLUMN CHART (For Matiere)
    // ==========================================
    class SimpleColumnChart extends JPanel {
        private Map<String, Integer> bars = new LinkedHashMap<>();

        public void addBar(String label, int value) { bars.put(label, value); }
        public void clearData() { bars.clear(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bars.isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g;
            
            // Layout calculations
            int padding = 40;
            int w = getWidth() - (2 * padding);
            int h = getHeight() - (2 * padding);
            int maxVal = 20; // Assume grades are out of 20
            
            int barWidth = (w / bars.size()) - 30; // space between bars
            int x = padding + 15;

            // Draw Base Line
            g2.setColor(Color.BLACK);
            g2.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);

            for (Map.Entry<String, Integer> entry : bars.entrySet()) {
                int barHeight = (int) ((double) entry.getValue() / maxVal * h);
                int y = getHeight() - padding - barHeight;

                // Draw Bar
                g2.setColor(new Color(220, 20, 60)); // Crimson Color
                g2.fillRect(x, y, barWidth, barHeight);
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, barWidth, barHeight);

                // Draw Text (Value on top)
                String valStr = String.valueOf(entry.getValue());
                g2.drawString(valStr, x + (barWidth/2) - 5, y - 5);

                // Draw Label (Bottom)
                g2.drawString(entry.getKey(), x, getHeight() - padding + 15);

                x += barWidth + 30;
            }
        }
    }
}