package library.solid;

import library.solid.repository.*;
import library.solid.service.DiscountLoanService;
import library.solid.service.LimitLoanService;
import library.solid.service.LoanService;

public class ApplicationInit {

    public MemberRepository memberRepository() {
        return new MemberRepositoryImpl();
    }

    public BookRepository bookRepository() {
        return new BookRepositoryImpl();
    }

    public LoanRepository loanRepository() {
        return new LoanRepositoryImpl();
    }

    /**
     * DiscountLoanService, LimitLoanService 중 하나만 return
     * @return
     */
    public LoanService loanService() {
        return new DiscountLoanService(memberRepository(), bookRepository(), loanRepository());
//        return new LimitLoanService(memberRepository(), bookRepository(), loanRepository());
    }
}
