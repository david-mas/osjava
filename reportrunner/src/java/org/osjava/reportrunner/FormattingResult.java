package org.osjava.reportrunner;

public class FormattingResult implements Result {

    private Result result;
    private Report report;

    public FormattingResult(Result result, Report report) {
        this.result = result;
        this.report = report;
    }

    public boolean hasNextRow() {
        return this.result.hasNextRow();
    }

    public Object[] nextRow() {
        Object[] row = this.result.nextRow();

        if(row == null || this.report.getColumns() == null) {
            return row;
        }

        for(int i=0; i<row.length; i++) {
            if(this.report.getColumns()[i].getFormatter() != null) {
                row[i] = this.report.getColumns()[i].getFormatter().format(row[i]);
            }
        }

        return row;
    }
}