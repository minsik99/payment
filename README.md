# StreamingPayment

**스트리밍된 동영상 수익 정산 프로젝트**

---

## 🚀 **프로젝트 개요**
StreamingPayment는 유튜브와 같은 스트리밍 플랫폼에서 동영상 스트리밍 데이터를 기반으로 수익 정산을 자동화하는 시스템입니다. 판매자(콘텐츠 제작자)에게 조회수, 광고 시청 횟수 등을 기반으로 정산된 수익을 제공하는 기능을 목표로 합니다.

---

## 🌐 **기능 개요**
### 1. **회원 관리**
- **회원가입**
  - 소셜 로그인 
  - 일반 회원과 판매자 계정 분리
- **로그인 및 로그아웃**
  - JWT 기반 인증 및 권한 관리

### 2. **동영상 관리**
- 동영상 업로드 및 조회
- 광고 등록 및 광고 수익 데이터 관리

### 3. **수익 정산**
- 조회수 기반 정산 데이터 생성
- 정산 기록 조회 기능 제공

### 4. **통계 기능**
- 판매자 대시보드
  - 동영상별 조회수 및 광고 시청 통계

---

## 🔧 **기술 스택**
- **Backend**: Java 21, Spring Boot 3.4
- **Database**: MySQL (Docker 기반)
- **Authentication**: Spring Security, JWT, OAuth2 (Google)
- **DevOps**: Docker, Docker Compose

---

## 🔄 **아키텍처**
**멀티모듈**
   - DDD 구조로 도메인 분리
   - API Gateway를 통한 MSA 구축
   - 모노레포 구조

---

## 📚 **ERD 설계**

![Untitled](https://github.com/user-attachments/assets/621bcf85-7bd7-4a7f-b4d1-73035d9e3497)

<details>
<summary style="font-size: 20px; font-weight: bold;">User Table</summary>
  <div markdown="1" style="font-size: 14px;"> <br/>
  
  **설명**: 사용자 정보 및 인증 관리를 위한 테이블

  | 컬럼명       | 타입               | 설명                          |
  |--------------|--------------------|-------------------------------|
  | id           | int [PK, Auto-Increment] | 사용자 ID (Primary Key)         |
  | username     | varchar(255)      | 사용자의 고유 이름              |
  | email        | varchar(255)      | 사용자의 이메일 주소 (고유값)     |
  | social_type  | varchar(255)      | 소셜 로그인 유형            |
  | role         | enum('USER', 'CREATOR') | 사용자 권한 (일반 사용자 또는 콘텐츠 제작자) |
  | created_at   | datetime          | 가입한 날짜                    |
  | updated_at   | datetime          | 마지막 업데이트 날짜             |

  </div>
</details>

<details>
<summary style="font-size: 20px; font-weight: bold;">Video Table</summary>
  <div markdown="1" style="font-size: 14px;"> <br/>
  
 **설명**: 동영상 정보를 관리하는 테이블

  | 컬럼명       | 타입               | 설명                          |
  |--------------|--------------------|-------------------------------|
  | id           | int [PK, Auto-Increment] | 동영상 ID (Primary Key)         |
  | user_id      | int [FK > user.id] | 동영상을 업로드한 사용자 ID       |
  | title        | varchar(255)      | 동영상 제목                    |
  | length       | int               | 동영상 길이 (초 단위)            |
  | description  | text              | 동영상 설명                    |
  | view_count  | int              | 동영상 조회수                  |
  | created_at   | datetime          | 동영상 등록일                  |
  | updated_at   | datetime          | 마지막 수정일                  |

  </div>
</details>

<details>
<summary style="font-size: 20px; font-weight: bold;">Video Play Table</summary>
  <div markdown="1" style="font-size: 14px;"> <br/>
  
 **설명**: 동영상 재생 기록을 저장하는 테이블

  | 컬럼명       | 타입               | 설명                          |
  |--------------|--------------------|-------------------------------|
  | id           | int [PK, Auto-Increment] | 재생 기록 ID (Primary Key)       |
  | user_id      | int [FK > user.id] | 동영상을 재생한 사용자 ID        |
  | video_id     | int [FK > video.id]| 재생된 동영상 ID                |
  | play_start   | datetime          | 재생 시작 시간                  |
  | play_end     | datetime          | 재생 종료 시간                  |
  | play_duration| int               | 재생 시간 (초 단위)             |
  | created_at   | datetime          | 기록 생성일                    |
  | updated_at   | datetime          | 기록 수정일                   |

  </div>
</details>

<details>
<summary style="font-size: 20px; font-weight: bold;">Advertise Table</summary>
  <div markdown="1" style="font-size: 14px;"> <br/>
  
 **설명**: 광고 정보를 관리하는 테이블

  | 컬럼명       | 타입               | 설명                          |
  |--------------|--------------------|-------------------------------|
  | id           | int [PK, Auto-Increment] | 광고 ID (Primary Key)          |
  | video_id     | int [FK > video.id]| 동영상 ID (Foreign Key)        |
  | created_at   | datetime          | 광고 등록일                   |
  | updated_at   | datetime          | 광고 수정일                   |

  </div>
</details>

<details>
<summary style="font-size: 20px; font-weight: bold;">Payment Table</summary>
  <div markdown="1" style="font-size: 14px;"> <br/>
  
 **설명**: 정산 정보를 관리하는 테이블

  | 컬럼명       | 타입               | 설명                          |
  |--------------|--------------------|-------------------------------|
  | id           | int [PK, Auto-Increment] | 정산 ID (Primary Key)          |
  | user_id      | int [FK > user.id] | 사용자 ID (Foreign Key)        |
  | video_id     | int [FK > video.id]| 동영상 ID (Foreign Key)        |
  | revenue      | decimal(10,2)     | 정산 금액                     |
  | view_count   | bigint            | 정산시 조회수                     |
  | payment_date | date              | 정산 날짜                     |
  | created_at   | datetime          | 기록 생성일                   |
  | updated_at   | datetime          | 정산 기록 수정일                     |

  </div>
</details>

---

## 📊 **기대 효과**
- 판매자가 자신의 콘텐츠 수익을 직관적으로 관리할 수 있는 시스템 제공
- 수익 정산의 투명성 및 신뢰성 향상
- 데이터 기반의 체계적인 통계 제공으로 판매자 성장 지원

---

## ⚡ **실행 방법**
1. Docker Compose로 MySQL 및 애플리케이션 실행:
   ```bash
   docker-compose up -d
   ```
---

## 🔎 **추가 개선 사항**
