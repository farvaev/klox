abstract class LoxExpr

data class LoxBinaryExpr(val left: LoxExpr, val operator: LoxToken, val right: LoxExpr): LoxExpr()

data class LoxUnaryExpr(val operator: LoxToken, val right: LoxExpr): LoxExpr()

data class LoxGroupingExpr(val expression: LoxExpr)

data class LoxLiteralExpr(val value: Any)
