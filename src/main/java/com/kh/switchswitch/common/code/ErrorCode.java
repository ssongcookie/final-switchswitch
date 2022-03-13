package com.kh.switchswitch.common.code;

public enum ErrorCode {
	
	DATABASE_ACCESS_ERROR("데이터베이스와 통신 중 에러가 발생하였습니다"),
	FAILED_VALIDATED_ERROR("데이터의 양식이 적합하지 않습니다"),
	MAIL_SEND_FAIL_ERROR("이메일 발송 중 에러가 발생하였습니다"),
	HTTP_CONNECT_ERROR("HTTP 통신 중 에러가 발생하였습니다"),
	AUTHENTICATION_FAILED_ERROR("유효하지 않은 인증입니다"),
	UNAUTHORIZED_PAGE("접근 권한이 없는 페이지 입니다"),
	REDIRECT_LOGIN_PAGE_NO_MESSAGE("","/member/login"),
	FAILED_FILE_UPLOAD_ERROR("파일업로드에 실해했습니다"),
	FAILED_TO_JOIN_WITH_KAKAO("이메일 제공은 필수 입니다.", "/member/login"),
	FAILED_TO_JOIN_LIMIT_DATE("탈퇴 후 3개월 이후에 재가입 가능합니다.","/"),
	FAILED_TO_KAKAO_LOGIN("카카오 로그인 중 오류가 발생하였습니다.","/member/login"),
	FAILED_TO_LOAD_INFO("해당 회원이 탈퇴처리되었거나 카드가 삭제되어 요청이 자동 취소되었습니다."),
	FAILED_TO_LEAVE_MEMBER("진행 중인 카드가 있어 탈퇴가 불가합니다.","/mypage/profile"),
	FAILED_TO_UPDATE_INFO("변경할 정보가 없습니다.","/admin/all-members"),
	FAILED_TO_DELETE_CARD("이미 삭제된 사진입니다.","/admin/real-time-cards"),
	FAILED_TO_DELETE_IMG("존재하지 않은 이미지 입니다.","/admin/real-time-card-img"), 
	FAILED_TO_LOAD_WITH_BAD_REQUEST("올바른 요청이 아닙니다."),
	FAILED_TO_REFUND_STATUSCODE_ALREADY_COMPLATE("이미 완료나 취소된 요청에 대해서는 변경할 수 없습니다.","/admin/refunds-history"),
	FAILED_TO_ACCESS_ADMIN_PROFILE("관리자 계정은 접근할 수 없습니다.","/admin/all-members");
	
	public final String MESSAGE;
	public final String URL;
	
	ErrorCode(String msg){
		this.MESSAGE = msg;
		this.URL = "/";
	}
	
	ErrorCode(String msg, String url){
		this.MESSAGE = msg;
		this.URL = url;
	}
	
}
