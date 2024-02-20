package com.lic.epgs.gratuity.quotation.member.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBulkStgRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.service.CommonService;

/**
 * @author Ismail Khan A
 *
 */
public class TaskThread implements Runnable {

	protected final Logger logger = LogManager.getLogger(getClass());
	


	private Long quotationId;

	private JdbcTemplate jdbcTemplate;

	private MemberRepository memberRepository;
	
	private MemberBulkStgRepository memberBulkStgRepository;
	
	

	private Long batchId;
	
	@Autowired
	private CommonService commonService;
	
	public synchronized String getLicSeq() {
		return commonService.getSequence(HttpConstants.LIC_SEQ);
	}

	public TaskThread(Long quotationId, JdbcTemplate jdbcTemplate, MemberRepository memberRepository,
			Long batchId) {
		logger.info("QuotationBulkMemberUploadHelper -- TaskThread --start");
		this.quotationId = quotationId;
		this.jdbcTemplate = jdbcTemplate;
		this.memberRepository = memberRepository;
		this.batchId = batchId;
	}

	@Override
	public void run() {
		saveMember();
	}

	public void saveMember() {
		logger.info("TaskThread -- saveMember --started");
		final List<MemberEntity> membersEn = new ArrayList<>();
		List<MemberBulkStgEntity> temps = getQuotationMembers(this.quotationId, this.batchId);
		for(MemberBulkStgEntity temp :temps){
			MemberEntity entity = new MemberEntity();
			org.springframework.beans.BeanUtils.copyProperties(temp, entity);
			entity.setQuotationId(quotationId);
			membersEn.add(entity);
		}
		
		if (membersEn.size() == temps.size()) {
			memberRepository.saveAll(membersEn);
			List<MemberBulkStgEntity> newTemps = new ArrayList<>();
			temps.forEach(tmp -> {
				tmp.setIsActive(false);
				newTemps.add(tmp);
			});
			memberBulkStgRepository.saveAll(newTemps);
		}
	}

	private List<MemberBulkStgEntity> getQuotationMembers(Long quotationId, Long batchId) {
		return jdbcTemplate.query(
				"SELECT * FROM QSTG_MEMBER_BULK_STG WHERE RECORD_STATUS='G' AND IS_ACTIVE=1 AND MEMBER_BATCH_ID=?",
				BeanPropertyRowMapper.newInstance(MemberBulkStgEntity.class), batchId);
	}
}
