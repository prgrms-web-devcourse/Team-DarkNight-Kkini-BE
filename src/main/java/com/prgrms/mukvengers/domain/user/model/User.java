package com.prgrms.mukvengers.domain.user.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;
import com.prgrms.mukvengers.global.utils.ValidateUtil;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users SET deleted = true where id=?")
public class User extends BaseEntity {

	public static final String DEFAULT_INTRODUCE = "자기소개를 작성해주세요";
	public static final Double DEFAULT_MANNER_VALUE = 36.5;
	public static final Integer ZERO = 0;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false)
	private String profileImgUrl;

	@Column(nullable = false, length = 100)
	private String introduction;

	@Column(nullable = false)
	private String provider;

	@Column(nullable = false)
	private String oauthId;

	@Column(nullable = false)
	private Double mannerScore;

	@Column(nullable = false)
	private Integer tasteScore;

	@Column(nullable = false)
	private Integer leaderCount;

	@Column(nullable = false)
	private Integer crewCount;

	@Column(nullable = false)
	private Integer reportedCount;

	@Column(nullable = false)
	private boolean enabled;

	@Builder
	protected User(String nickname, String profileImgUrl, String provider, String oauthId) {
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
		ValidateUtil.checkText(profileImgUrl, "유효하지 않은 URL");
		ValidateUtil.checkOverLength(profileImgUrl, 255, "최대 글자수를 초과했습니다.");
		return profileImgUrl;
	}

	private String validateIntroduction(String introduction) {
		ValidateUtil.checkText(introduction, "유효하지 않는 자기소개");
		ValidateUtil.checkOverLength(introduction, 255, "최대 글자수를 초과했습니다.");
		return introduction;
	}

	public User changeProfile(UpdateUserRequest updateUserRequest) {
		this.nickname = validateNickName(updateUserRequest.nickName());
		this.profileImgUrl = validateProfileImgUrl(updateUserRequest.profileImgUrl());
		this.introduction = validateIntroduction(updateUserRequest.introduction());
		return this;
	}

	public boolean isSameUser(Long userId) {
		return Objects.equals(this.id, userId);
	}

	public void addMannerScore(Integer mannerScore) {
		this.mannerScore = (0.1 * mannerScore);
	}

	public void addTasteScore(Integer tasteScore) {
		this.tasteScore += tasteScore;
	}
}
