package com.lic.epgs.gratuity.policyservices.dom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.dom.entity.PmstMidleaverMemberPropsEntity;

@Repository
public interface MidleaverMemberPropsRepository extends JpaRepository<PmstMidleaverMemberPropsEntity, Long> {

	@Query(value = "SELECT * FROM PMST_MIDLEAVER_MEMBER_PROPS WHERE TMP_MEMBER_ID=?1", nativeQuery = true)
	PmstMidleaverMemberPropsEntity findByTmpMemberId(Long tmpMemberId);

	@Query(value = "SELECT * FROM PMST_MIDLEAVER_MEMBER_PROPS A "
			+ "JOIN PMST_TMP_MEMBER B ON B.MEMBER_ID=A.TMP_MEMBER_ID " + "WHERE B.TMP_POLICY_ID=?1", nativeQuery = true)
	List<PmstMidleaverMemberPropsEntity> findByTmpPolicyId(Long tmpPolicyId);

	@Query(value = "SELECT count(*) FROM PMST_MIDLEAVER_MEMBER_PROPS A \r\n"
			+ "         JOIN PMST_TMP_MEMBER B ON B.MEMBER_ID=A.TMP_MEMBER_ID \r\n"
			+ "			WHERE B.TMP_POLICY_ID=?1", nativeQuery = true)
	Long findByTmpPolicyIdCount(Long tmpPolicyId);

}
