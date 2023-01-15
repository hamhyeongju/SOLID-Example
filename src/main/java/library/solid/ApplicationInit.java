package library.solid;

import library.solid.repository.*;

public class ApplicationInit {

    public MemberRepository memberRepository() {
        return new MemberRepositoryImpl();
    }

    public BookRepository bookRepository() {
        return new BookRepositoryImpl();
    }

    public LoanRepository loanService() {
        return new LoanRepositoryImpl();
    }
}
