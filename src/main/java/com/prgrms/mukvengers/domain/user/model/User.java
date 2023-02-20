package com.prgrms.mukvengers.domain.user.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.global.common.domain.BaseEntity;
import com.prgrms.mukvengers.global.utils.ValidateUtil;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE users SET deleted = true where id=?")
@Table(name = "users")
@Entity
public class User extends BaseEntity {

	public static final String DEFAULT_INTRODUCE = "자기소개를 작성해주세요";
	public static final Double DEFAULT_MANNER_VALUE = 36.5;
	public static final Integer ZERO = 0;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Size(max = 100)
	private String nickname;

	@Size(max = 255)
	private String profileImgUrl;

	@Size(max = 255)
	private String introduction;

	@Size(max = 255)
	private String provider;

	@Size(max = 255)
	private String oauthId;

	@PositiveOrZero
	private Double mannerScore;

	@PositiveOrZero
	private Integer tasteScore;

	@PositiveOrZero
	private Integer leaderCount;

	@PositiveOrZero
	private Integer crewCount;

	@PositiveOrZero
	private Integer reportedCount;

	private boolean enabled;

	@Builder
	public User(String nickname, String profileImgUrl, String provider, String oauthId) {
		this.nickname = validateNickName(nickname);
		this.profileImgUrl = validateProfileImgUrl(profileImgUrl);
		this.provider = provider;
		this.oauthId = oauthId;
		this.introduction = DEFAULT_INTRODUCE;
		this.mannerScore = DEFAULT_MANNER_VALUE;
		this.tasteScore = ZERO;
		this.leaderCount = ZERO;
		this.crewCount = ZERO;
		this.reportedCount = ZERO;
	}

	private String validateNickName(String nickName) {
		ValidateUtil.checkText(nickName, "유효하지 않은 닉네임");
		ValidateUtil.checkOverLength(nickName, 100, "최대 글자수를 초과했습니다.");
		return nickName;
	}

	private String validateProfileImgUrl(String profileImgUrl) {
		ValidateUtil.checkText(profileImgUrl, "유효하지 않은 이미지 URL");
		return profileImgUrl;
	}

}
