package com.board.study.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.board.study.entity.board.member.Member;
import com.board.study.entity.board.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			
		Member member = memberRepository.findByEmail(email);
		
		if (member == null) throw new UsernameNotFoundException("Not Found account."); 
		
		return member;
	}
}