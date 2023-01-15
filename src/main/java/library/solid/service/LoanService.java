package library.solid.service;

import library.solid.domain.Member;

public interface LoanService {

    void loan(Long memberId, Long bookId);

}
