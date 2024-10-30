package com.LRG.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.LRG.domain.Report;
import com.LRG.model.ReportDto;
import com.LRG.repository.ReportRepository;

@Service
public class ReportService {

private ReportRepository reportRepository;
	
	public ReportService(ReportRepository reportRepository) {
		super();
		this.reportRepository = reportRepository;
	}
	
	public List<ReportDto> getReports(){
		List<ReportDto> reportDtoList = new ArrayList<ReportDto>();
		List<Report> reports = reportRepository.findAllByOrderByYearDesc();
		if(reports != null && !reports.isEmpty()) {
			for(Report report : reports) {
				ReportDto reportDto = convertDomainToDto(report);
				if(reportDto!=null) reportDtoList.add(reportDto);				
			}
		}
		return reportDtoList;	
	}

	private ReportDto convertDomainToDto(Report report) {
		return new ReportDto(report.getPublisher(),report.getDiscussionArea(),report.getYear(),report.getLink());
	}

	public void addReport(String p, String d, int y, String l) {
		reportRepository.save(new Report(p,d,y,l));			
	}
}
