package com.bornfire.entity;

public enum TranMonitorStatus {

///Message Type
OUTGOING,
INCOMING,
RTP,
BULK_CREDIT,
MANUAL,
BULK_DEBIT,
OUTWARD_BULK_RTP,

VALIDATION_ERROR,

/////CBS Status
CBS_DEBIT_INITIATED,
CBS_DEBIT_OK,
CBS_DEBIT_ERROR,
CBS_DEBIT_REVERSE_INITIATED,
CBS_DEBIT_REVERSE_OK,
CBS_DEBIT_REVERSE_ERROR,

CBS_CREDIT_INITIATED,
CBS_CREDIT_OK,
CBS_CREDIT_ERROR,
CBS_CREDIT_REVERSE_INITIATED,
CBS_CREDIT_REVERSE_OK,
CBS_CREDIT_REVERSE_ERROR,
CBS_SERVER_NOT_CONNECTED,
WAITING_FOR_DEBIT_REVERSE,
WAITING_FOR_CREDIT_REVERSE,

///IPSX Status
IPSX_OUTMSG_INITIATED,
IPSX_OUTMSG_ACK_RECEIVED,
IPSX_OUTMSG_NAK_RECEICED,
IPSX_INMSG_ACK_RECEIVED,
IPSX_INMSG_NAK_RECEICED,
IPSX_RESPONSE_RECEIVED,
IPSX_RTP_INITIATED,
IPSX_RTP_ACK_RECEIVED,
IPSX_RTP_NAK_RECEICED,
IPSX_RESPONSE_ACSP,
IPSX_RESPONSE_RJCT,
IPSX_ERROR,
IPSX_NOT_CONNECTED,

///IPSX Response Status
ACSP,
RJCT,

///Transaction Status
INITIATED,
IN_PROGRESS,
SUCCESS,
FAILURE,
REVERSE_FAILURE,

///Consent Values
Authorised,
AwaitingAuthorisation,
Rejected,
Revoked,

///Permissions
ReadAccountsDetails,
ReadBalances,
ReadTransactionsDetails,
DebitAccount,
Consent_creation,
ConsentAuthorisation,
ConsentDelete,

///Credit Debit Indicator
Credit,
Debit,

///Acct Bal Type
InterimAvailable,

////Bulk RTP Status
RTP_Initiated,

///Local Instrumentation
CSDC,
BPDC,

}
