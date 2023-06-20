import javax.swing.table.AbstractTableModel;

class CalendarTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final Object[][] data;

    public CalendarTableModel(int month, int year) {
        data = new Object[6][7];
        java.time.LocalDate date = java.time.LocalDate.of(year, month, 1);
        int dayOfWeek = date.getDayOfWeek().getValue() % 7;
        int daysInMonth = date.getMonth().length(java.time.Year.isLeap(year));
        int day = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 0 && j < dayOfWeek) {
                    data[i][j] = "";
                } else if (day > daysInMonth) {
                    data[i][j] = "";
                } else {
                    data[i][j] = day;
                    day++;
                }
            }
        }
    }

    public int getRowCount() {
        return 6;
    }

    public int getColumnCount() {
        return 7;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
}
