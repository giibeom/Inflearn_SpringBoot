package hello.core.member;

/**
 * @author Created by 명기범 on 2021-11-29
 */
public interface MemberService {

    void join(Member member);

    Member findMember(Long memberId);
}
