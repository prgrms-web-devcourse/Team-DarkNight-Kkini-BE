// package com.prgrms.mukvengers.domain.post.model;
//
// import static javax.persistence.FetchType.*;
// import static javax.persistence.GenerationType.*;
// import static lombok.AccessLevel.*;
//
// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.OneToOne;
//
// import org.hibernate.annotations.SQLDelete;
// import org.hibernate.annotations.Where;
//
// import com.prgrms.mukvengers.domain.crew.model.Crew;
// import com.prgrms.mukvengers.domain.user.model.User;
// import com.prgrms.mukvengers.global.common.domain.BaseEntity;
//
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
//
// @Entity
// @Getter
// @NoArgsConstructor(access = PROTECTED)
// @Where(clause = "deleted = false")
// @SQLDelete(sql = "UPDATE post set deleted = true where id=?")
// public class Post extends BaseEntity {
//
// 	@Id
// 	@GeneratedValue(strategy = IDENTITY)
// 	private Long id;
//
// 	@OneToOne(fetch = LAZY)
// 	@JoinColumn(name = "crew_id", referencedColumnName = "id")
// 	private Crew crew;
//
// 	@OneToOne(fetch = LAZY)
// 	@JoinColumn(name = "user_id", referencedColumnName = "id")
// 	private User user;
//
// 	@Column(nullable = false, length = 500)
// 	private String content;
//
// 	@Builder
// 	protected Post(Crew crew, User user, String content) {
// 		this.crew = crew;
// 		this.user = user;
// 		this.content = content;
// 	}
// }
