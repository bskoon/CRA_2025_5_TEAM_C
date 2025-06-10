## 🔍 개요
<!-- 어떤 기능/수정인지 한 줄 요약해주세요 -->
- 예: VirtualSSD 예외 처리 보완 및 fullwrite 명령어 구현

---

## ✅ 작업 내용 체크리스트
- [ ] 기능 구현 (Command: W / R / fullwrite / fullread)
- [ ] 예외 처리 (`ERROR` 출력 통일, 대소문자 검증 등)
- [ ] 테스트 코드 작성 및 통과 (JUnit5 + Mockito)
- [ ] Gradle 빌드 및 실행 확인 (`ssd-app.jar`, `test-shell.jar`)
- [ ] 문서/README/명세서 반영

---

## 💡 상세 설명
<!-- 구현 방식, 고려한 점, 한계 등 자유롭게 기술해주세요 -->
- 예: `TestShell`에 InputReader/OutputWriter 분리하여 테스트 가능하게 개선

---

## 🧪 테스트 결과
<!-- 테스트 수행 결과를 요약해주세요 -->
- 예: `TestShellTest` 정상 통과 (PASS)
- 예외 상황 시 `"ERROR"` 출력 확인

---

## 🔁 관련 이슈
<!-- 관련된 이슈 번호 또는 작업 항목 -->
- close #10
- ref #5

---

## 📎 기타 참고 사항
<!-- 리뷰어가 참고할만한 정보 (스크린샷, 로그, 참고 링크 등) -->
