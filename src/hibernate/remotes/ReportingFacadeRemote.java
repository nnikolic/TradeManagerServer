package hibernate.remotes;

import java.util.Map;

import javax.ejb.Remote;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Remote
public interface ReportingFacadeRemote {
	public JasperReport getJasperReport(String reportName);
	public JasperPrint fillJasperReport(JasperReport report, Map<String, Object> map, JRDataSource processor);
}
