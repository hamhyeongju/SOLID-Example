package library.solid;

import library.solid.repository.*;
import library.solid.service.DiscountLoanService;
import library.solid.service.LoanService;

public class ApplicationInit {

    public MemberRepository memberRepository() {
        return new MemberRepositoryImpl();
    }

    public BookRepository bookRepository() {
        return new BookRepositoryImpl();
    }

    public LoanService loanService() {
        return new DiscountLoanService(memberRepository(), bookRepository());
    }
}
