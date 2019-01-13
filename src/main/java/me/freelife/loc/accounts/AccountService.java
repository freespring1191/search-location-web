package me.freelife.loc.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * PasswordEncoder를 통해 인코딩한 패스워드를 저장
     * @param account
     * @return
     */
    public Account saveAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }

    /**
     * Account를 스프링 시큐리티가 이해할 수 있는 타입인 UserDetails로 변환
     * 스프링 시큐리티가 제공하는 User라는 클래스를 사용해서 구현하면 전체 인터페이스를 다 구현하지 않아도 되어서 편리
     * AccountService의 UserDetails를 AccountAdapter로 리턴
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)); // account 객체가 없으면 UsernameNotFoundException 에러를 던짐
        return new AccountAdapter(account);
    }

}
