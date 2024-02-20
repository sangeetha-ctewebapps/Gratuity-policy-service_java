package com.lic.epgs.gratuity.policyservices.common.dao;

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

import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceMatrixDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PolicyServiceMatrixDaoImpl {

	JdbcTemplate jdbcTemplate;

	@Autowired
	public PolicyServiceMatrixDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	protected final Logger logger = LogManager.getLogger(getClass());

	public List<PolicyServiceMatrixDto> getMatrix() {
		
		String query = " select CURRENT_SERVICE,ONGOING_SERVICE,IS_ALLOWED,IS_ACTIVE from POLICY_SERVICE_MATRIX_NEW where is_active = 1 ";
		logger.info("tempPolicyModificationDataTableResponseDtoList ---   {}", query);
		jdbcTemplate.setFetchSize(900000);
		
		return jdbcTemplate.query(query.toString(),
				new Object[] { },
				new int[] {},
				new RowMapper<PolicyServiceMatrixDto>() {
					@Override
					public PolicyServiceMatrixDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						PolicyServiceMatrixDto policyServiceMatrixDto = new PolicyServiceMatrixDto();

						policyServiceMatrixDto.setCurrentService(StringUtils.trim(rs.getString("CURRENT_SERVICE")));
						policyServiceMatrixDto.setOngoingService(StringUtils.trim(rs.getString("ONGOING_SERVICE")));
						policyServiceMatrixDto.setIsAllowed(StringUtils.trim(rs.getString("IS_ALLOWED")));
						policyServiceMatrixDto.setIsActive(rs.getLong("IS_ACTIVE"));
						
			
						return policyServiceMatrixDto;
					}
				});
		
	}

	public List<PolicyServiceMatrixDto> getMatrix(String currentService, String serviceType) {
		String query = " select CURRENT_SERVICE,ONGOING_SERVICE,IS_ALLOWED,IS_ACTIVE \r\n"
				+ "from POLICY_SERVICE_MATRIX_NEW \r\n"
				+ "where is_active = 1 \r\n"
				+ "and \r\n"
				+ "UPPER(CURRENT_SERVICE) = UPPER(?) \r\n"
				+ "and\r\n"
				+ "UPPER(ONGOING_SERVICE) = UPPER(?) ";
		logger.info("tempPolicyModificationDataTableResponseDtoList ---   {}", query);
		jdbcTemplate.setFetchSize(900000);
		
		return jdbcTemplate.query(query.toString(),
				new Object[] {currentService, serviceType},
				new int[] {Types.VARCHAR, Types.VARCHAR},
				new RowMapper<PolicyServiceMatrixDto>() {
					@Override
					public PolicyServiceMatrixDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						PolicyServiceMatrixDto policyServiceMatrixDto = new PolicyServiceMatrixDto();

						policyServiceMatrixDto.setCurrentService(StringUtils.trim(rs.getString("CURRENT_SERVICE")));
						policyServiceMatrixDto.setOngoingService(StringUtils.trim(rs.getString("ONGOING_SERVICE")));
						policyServiceMatrixDto.setIsAllowed(StringUtils.trim(rs.getString("IS_ALLOWED")));
						policyServiceMatrixDto.setIsActive(rs.getLong("IS_ACTIVE"));
						
			
						return policyServiceMatrixDto;
					}
				});
		
	}
}
