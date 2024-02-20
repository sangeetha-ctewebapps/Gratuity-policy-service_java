package com.lic.epgs.gratuity.policyservices.policymodification.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.policymodification.dao.MPHModificationDao;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableRequestDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableResponseDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PolicyModificationDaoImpl implements MPHModificationDao {
	JdbcTemplate jdbcTemplate;

	@Autowired
	public PolicyModificationDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	protected final Logger logger = LogManager.getLogger(getClass());

	@Override
	public List<TempPolicyModificationDataTableResponseDto> tempPolicyModificationDataTableResponseDtoList(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto, Long statusId) {

		String query = "SELECT a.* " + ",ceil(total_count/10) no_of_pages "
				+ "FROM (	select policy.policy_id as POLICY_ID, policy.policy_number as POLICY_NUMBER, mph.mph_name as MPH_NAME, mph.mph_code as MPH_CODE, policy.POLICY_STATUS_ID as STATUS "
				+ ",row_number() over (order by policy.policy_id) as rowno,	" + "count(1) over () total_count	"
				+ "from PMST_TMP_POLICY policy, " + "PMST_TMP_MPH mph " + "where 1=1 "
				+ "and policy.mph_id= mph.mph_id " + "AND policy.policy_status_id = ? "
				+ ")a	where rowno >= ? and rowno <= ? ";
		logger.info("tempPolicyModificationDataTableResponseDtoList ---   {}", query);
		jdbcTemplate.setFetchSize(900000);

		return jdbcTemplate.query(query.toString(),
				new Object[] {statusId, tempPolicyModificationDataTableRequestDto.getStart(),
						tempPolicyModificationDataTableRequestDto.getEnd() },
				new int[] {Types.INTEGER, Types.INTEGER, Types.INTEGER },
				new RowMapper<TempPolicyModificationDataTableResponseDto>() {
					@Override
					public TempPolicyModificationDataTableResponseDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TempPolicyModificationDataTableResponseDto tempPolicyModificationDataTableResponseDto = new TempPolicyModificationDataTableResponseDto();

						tempPolicyModificationDataTableResponseDto
								.setPolicyId(StringUtils.trim(rs.getString("POLICY_ID")));

						tempPolicyModificationDataTableResponseDto
								.setMphCode(StringUtils.trim(rs.getString("MPH_CODE")));
						tempPolicyModificationDataTableResponseDto
								.setMphName(StringUtils.trim(rs.getString("MPH_NAME")));
						tempPolicyModificationDataTableResponseDto
								.setPolicyNumber(StringUtils.trim(rs.getString("POLICY_NUMBER")));
						
						tempPolicyModificationDataTableResponseDto.setStatus(rs.getLong("STATUS"));

						tempPolicyModificationDataTableResponseDto.setTotalRecords(rs.getLong("TOTAL_COUNT"));
						tempPolicyModificationDataTableResponseDto.setNoOfPages(rs.getLong("NO_OF_PAGES"));

						return tempPolicyModificationDataTableResponseDto;
					}
				});
	}

	@Override
	public List<TempPolicyModificationDataTableResponseDto> getPolicyModifcationExisting(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto, Long status1,
			Long status2) {
		String query = "SELECT a.* " + ",ceil(total_count/10) no_of_pages "
				+ "FROM (	select policy.policy_id as POLICY_ID, policy.policy_number as POLICY_NUMBER, mph.mph_name as MPH_NAME, mph.mph_code as MPH_CODE , policy.POLICY_STATUS_ID as STATUS "
				+ ",row_number() over (order by policy.policy_id) as rowno,	" + "count(1) over () total_count	"
				+ "from PMST_TMP_POLICY policy, " + "PMST_TMP_MPH mph " + "where 1=1 "
				+ "and policy.mph_id= mph.mph_id " + "AND ( policy.policy_status_id = ? or policy.policy_status_id = ? ) "
				+ ")a	where rowno >= ? and rowno <= ? ";
		logger.info("tempPolicyModificationDataTableResponseDtoList ---   {}", query);
		jdbcTemplate.setFetchSize(900000);

		return jdbcTemplate.query(query.toString(),
				new Object[] {status1, status2, tempPolicyModificationDataTableRequestDto.getStart(),
						tempPolicyModificationDataTableRequestDto.getEnd() },
				new int[] {Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER },
				new RowMapper<TempPolicyModificationDataTableResponseDto>() {
					@Override
					public TempPolicyModificationDataTableResponseDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TempPolicyModificationDataTableResponseDto tempPolicyModificationDataTableResponseDto = new TempPolicyModificationDataTableResponseDto();

						tempPolicyModificationDataTableResponseDto
								.setPolicyId(StringUtils.trim(rs.getString("POLICY_ID")));

						tempPolicyModificationDataTableResponseDto
								.setMphCode(StringUtils.trim(rs.getString("MPH_CODE")));
						tempPolicyModificationDataTableResponseDto
								.setMphName(StringUtils.trim(rs.getString("MPH_NAME")));
						tempPolicyModificationDataTableResponseDto
								.setPolicyNumber(StringUtils.trim(rs.getString("POLICY_NUMBER")));
						
						tempPolicyModificationDataTableResponseDto.setStatus(rs.getLong("STATUS"));

						tempPolicyModificationDataTableResponseDto.setTotalRecords(rs.getLong("TOTAL_COUNT"));
						tempPolicyModificationDataTableResponseDto.setNoOfPages(rs.getLong("NO_OF_PAGES"));

						return tempPolicyModificationDataTableResponseDto;
					}
				});

	}

	@Override
	public List<TempPolicyModificationDataTableResponseDto> getPolicyModifcationMakerInprogress(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto, Long draft,
			Long pendingForApproval, Long sendToMaker) {
		String query = "SELECT a.* " + ",ceil(total_count/10) no_of_pages "
				+ "FROM (	select policy.policy_id as POLICY_ID, policy.policy_number as POLICY_NUMBER, mph.mph_name as MPH_NAME, mph.mph_code as MPH_CODE , policy.POLICY_STATUS_ID as STATUS "
				+ ",row_number() over (order by policy.policy_id) as rowno,	" + "count(1) over () total_count	"
				+ "from PMST_TMP_POLICY policy, " + "PMST_TMP_MPH mph " + "where 1=1 "
				+ "and policy.mph_id= mph.mph_id " + "AND ( policy.policy_status_id = ? or policy.policy_status_id = ? or policy.policy_status_id = ? ) "
				+ ")a	where rowno >= ? and rowno <= ? ";
		logger.info("tempPolicyModificationDataTableResponseDtoList ---   {}", query);
		jdbcTemplate.setFetchSize(900000);

		return jdbcTemplate.query(query.toString(),
				new Object[] {draft, pendingForApproval, sendToMaker, tempPolicyModificationDataTableRequestDto.getStart(),
						tempPolicyModificationDataTableRequestDto.getEnd() },
				new int[] {Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER },
				new RowMapper<TempPolicyModificationDataTableResponseDto>() {
					@Override
					public TempPolicyModificationDataTableResponseDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TempPolicyModificationDataTableResponseDto tempPolicyModificationDataTableResponseDto = new TempPolicyModificationDataTableResponseDto();

						tempPolicyModificationDataTableResponseDto
								.setPolicyId(StringUtils.trim(rs.getString("POLICY_ID")));

						tempPolicyModificationDataTableResponseDto
								.setMphCode(StringUtils.trim(rs.getString("MPH_CODE")));
						tempPolicyModificationDataTableResponseDto
								.setMphName(StringUtils.trim(rs.getString("MPH_NAME")));
						tempPolicyModificationDataTableResponseDto
								.setPolicyNumber(StringUtils.trim(rs.getString("POLICY_NUMBER")));
						
						tempPolicyModificationDataTableResponseDto.setStatus(rs.getLong("STATUS"));

						tempPolicyModificationDataTableResponseDto.setTotalRecords(rs.getLong("TOTAL_COUNT"));
						tempPolicyModificationDataTableResponseDto.setNoOfPages(rs.getLong("NO_OF_PAGES"));

						return tempPolicyModificationDataTableResponseDto;
					}
				});
	}

	
}
