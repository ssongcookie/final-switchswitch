package com.kh.switchswitch.common.util.pagination;

public class PagingV2 {
	// 현재페이지, 시작페이지, 끝페이지, 게시글 총 갯수, 페이지당 글 갯수, 마지막페이지, start, end, prev, next, 블록 갯수
		private int nowPage, startAlarm, endAlarm, total, cntPerPage, lastPage, start, end, prev, next;
		private int blockCnt = 5;
		private String url;
		
		public PagingV2() {
		}
		public PagingV2(int total, int nowPage, int cntPerPage, String url) {
			setNowPage(nowPage);
			setCntPerPage(cntPerPage);
			setTotal(total);
			setUrl(url);
			calcLastPage(getTotal(), getCntPerPage());
			calcStartAlarmEndAlarm(getNowPage(), getCntPerPage());
			calcStartEnd(getNowPage(), getLastPage());
			calcPrevAndNext(getStart(), getEnd(), getLastPage());
		}
		// 제일 마지막 페이지 계산
		public void calcLastPage(int total, int cntPerPage) {
			int lastPage = (int)Math.ceil((double)total/cntPerPage);
			if(lastPage == 0) {
				lastPage = 1;
			}
			setLastPage(lastPage);
		}
		// 현재 페이지 기준 시작, 끝 게시글 계산
		public void calcStartAlarmEndAlarm(int nowPage, int cntPerPage) {
			this.startAlarm = nowPage == 1 ? 1 : (nowPage-1)*cntPerPage+1;
			int end = startAlarm + cntPerPage -1;
			this.endAlarm = end > total ? total : end;
			System.out.println("startAlarm:" + startAlarm + ", endAlarm:" + endAlarm);
		}
		
		// 현재 페이지가 있는 블록의 처음, 끝
		public void calcStartEnd(int nowPage, int lastPage) {
			this.start = Math.abs(nowPage/blockCnt)*blockCnt+1;
			int endBlock = start + blockCnt -1;
			this.end = endBlock > lastPage ? lastPage : endBlock;
			System.out.println("start:" + start + ", end:" + end);
		}
		
		// 이전 버튼 클릭 시 이동할 페이지 / 다음 버튼 클릭 시 이동할 페이지
		public void calcPrevAndNext(int start, int end, int lastPage) {
			this.prev = start-1 > 0 ? start-1 : 1;
			int endPage = end + 1;
			this.next = endPage > lastPage ? lastPage : endPage;
			System.out.println("prev:" + prev + ", next:" + next);
		}
		
		public int getNowPage() {
			return nowPage;
		}
		public void setNowPage(int nowPage) {
			this.nowPage = nowPage;
		}
		public int getStartAlarm() {
			return startAlarm;
		}
		public void setStartAlarm(int startAlarm) {
			this.startAlarm = startAlarm;
		}
		public int getEndAlarm() {
			return endAlarm;
		}
		public void setEndAlarm(int endAlarm) {
			this.endAlarm = endAlarm;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public int getCntPerPage() {
			return cntPerPage;
		}
		public void setCntPerPage(int cntPerPage) {
			this.cntPerPage = cntPerPage;
		}
		public int getLastPage() {
			return lastPage;
		}
		public void setLastPage(int lastPage) {
			this.lastPage = lastPage;
		}
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
		public int getPrev() {
			return prev;
		}
		public void setPrev(int prev) {
			this.prev = prev;
		}
		public int getNext() {
			return next;
		}
		public void setNext(int next) {
			this.next = next;
		}
		public int getBlockCnt() {
			return blockCnt;
		}
		public void setBlockCnt(int blockCnt) {
			this.blockCnt = blockCnt;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		@Override
		public String toString() {
			return "PagingV2 [nowPage=" + nowPage + ", startAlarm=" + startAlarm + ", endAlarm=" + endAlarm + ", total="
					+ total + ", cntPerPage=" + cntPerPage + ", lastPage=" + lastPage + ", start=" + start + ", end="
					+ end + ", prev=" + prev + ", next=" + next + ", blockCnt=" + blockCnt + ", url=" + url + "]";
		}
		
}
