package com.lic.epgs.gratuity.accountingservice.service;

import java.util.List;
import java.util.Map;

import com.lic.epgs.gratuity.accountingservice.dto.AcctClaimResponseDto;
import com.lic.epgs.gratuity.accountingservice.dto.ClaimLiabilityBookingReqDto;
import com.lic.epgs.gratuity.accountingservice.dto.ClaimReverseLiabilityReqDto;
import com.lic.epgs.gratuity.accountingservice.dto.GlTransactionModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.IssuePolicyDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.MidLeaverContributuionPremuimAndGstDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContributionAndLifeCoverAdjustmentDto;
import com.lic.epgs.gratuity.accountingservice.dto.RenewalContributionAdjustRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderAccountingIntegrationRequestModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.GlTransactionModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.MergePolicyRequestModel;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;

public interface AccountingService {

	// NB
	ApiResponseDto<List<DepositDto>> getDeposits(String proposalNo, String policyNo, String username);

	ResponseDto lockDeposits(List<ShowDepositLockDto> showDepositLockDto, String username);

	ResponseDto unlockDeposits(List<UnlockDepositDetailDto> showDepositLockDto, String username);

	HSNCodeDto getHSNCode();

	void policyIssuance(IssuePolicyDto issuePolicyDto);

	Boolean consumeDeposits(
			NewBusinessContributionAndLifeCoverAdjustmentDto newBusinessContributionAndLifeCoverAdjustmentDto, Long masterId);

	Map<String, Double> getGstComponents(String unitStateType, String mphStateType, HSNCodeDto hSNCodeDto,
			Double adjustment);

	// Claims Liability Booking
	AcctClaimResponseDto intimateDeathClaim(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto, String username,
			String module, Long referenceId, String referenceValue);

	AcctClaimResponseDto intimateDeathClaimforNonCourt(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue);

	// Claims Liability Booking Reversal
	AcctClaimResponseDto reverseDeathClaimIntimation(ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto,
			String username, String module, Long referenceId, String referenceValue);

	AcctClaimResponseDto reverseDeathClaimIntimationNonCourtCase(
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue);

	AcctClaimResponseDto intimateMaturityClaim(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto, String username,
			String module, Long referenceId, String referenceValue);

	AcctClaimResponseDto intimateMaturityClaimforNonCourt(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue);

	AcctClaimResponseDto reverseMaturityClaimIntimation(ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto,
			String username, String module, Long referenceId, String referenceValue);

	AcctClaimResponseDto reverseWithdrawalClaimIntimation(ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto,
			String username, String module, Long referenceId, String referenceValue);

	AcctClaimResponseDto reverseWithdrawalClaimIntimationforNonCourt(
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue);

	GlTransactionModelDto getGlTransactionModel(Long productId, Long variantId, String unitCode, String naration);

	JournalVoucherDetailModelDto getJournalVoucherDetailModel(String lineOfBusiness, Long productId, Long variantId);

	AcctClaimResponseDto reverseMaturityClaimIntimationforNonCourt(
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue);

	AcctClaimResponseDto intimateWithdrawalClaim(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue);

	AcctClaimResponseDto intimateWithdrawalClaimforNonClaim(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue);

	void consumeDeposit(RenewalContributionAdjustRequestModelDto renewalContributionAdjustRequestModelDto, Long long1);

	ResponseDto commonmasterserviceAllUnitCode(String unitCode);

	Double getGSTRate(String unitStateType, String mPHStateType, HSNCodeDto hSNCodeDto);

	void consumeDepositsforSubsequence(
			NewBusinessContributionAndLifeCoverAdjustmentDto newBusinessContributionAndLifeCoverAdjustmentDto, Long masterId);

	GlTransactionModel glTransactionModel(Long productId, Long productVariantId, String unitCode, String string);

	void consumeDeposit(MergePolicyRequestModel mergePolicyRequestModel, Long long1);

	Boolean policySurrender(SurrenderAccountingIntegrationRequestModel surrenderAccountingIntegrationRequestModel);

	void domAccounting(MidLeaverContributuionPremuimAndGstDto midLeaverContributuionPremuimAndGstDto);

}
