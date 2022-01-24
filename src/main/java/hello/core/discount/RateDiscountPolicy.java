package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

/**
 * @author Created by 명기범 on 2021-12-06
 */
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPrecent = 10;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPrecent / 100;
        } else {
            return 0;
        }
    }
}