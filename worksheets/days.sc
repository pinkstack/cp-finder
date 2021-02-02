import java.time.LocalDate
import java.time.temporal.ChronoUnit

val p = LocalDate.now()

val x = LocalDate.now().minusDays(-14)

ChronoUnit.DAYS.between(p, x)
ChronoUnit.DAYS.between(p, LocalDate.now().plusDays(30))
