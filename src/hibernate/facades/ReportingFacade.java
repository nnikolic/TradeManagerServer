package hibernate.facades;

import java.util.Map;

import javax.ejb.Stateless;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import hibernate.remotes.ReportingFacadeRemote;

@Stateless
public class ReportingFacade implements ReportingFacadeRemote{

//	public static String REPORT_ROOT_URL = "C:\\Users\\Nenad\\Documents\\Raf\\Java\\Diplomski\\DiplomskiMetadata\\src\\reports\\";
	public static String REPORT_ROOT_URL = MetadataFacade.METADATA_URL+"reports\\"; 
	
	public JasperReport getJasperReport(String reportName) {
		JasperReport report = null;
		try {
			report = JasperCompileManager.compileReport(REPORT_ROOT_URL+reportName);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return report;
	}
	
	@Override
	public JasperPrint fillJasperReport(JasperReport report,
			Map<String, Object> map, JRDataSource processor) {
		// TODO Auto-generated method stub
		try {
			return JasperFillManager.fillReport(report, map, processor);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
