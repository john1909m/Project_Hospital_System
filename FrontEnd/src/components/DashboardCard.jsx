import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";

export function DashboardCard({
  title,
  value,
  icon: Icon,
  description,
  trend,
  variant = "default",
}) {
  const variantStyles = {
    default: "bg-card",
    primary: "bg-primary/5 border-primary/20",
    success: "bg-success/5 border-success/20",
    warning: "bg-warning/5 border-warning/20",
    destructive: "bg-destructive/5 border-destructive/20",
  };

  const iconStyles = {
    default: "text-primary",
    primary: "text-primary",
    success: "text-success",
    warning: "text-warning",
    destructive: "text-destructive",
  };

  return (
    <Card className={cn("hover-lift border", variantStyles[variant])}>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium text-muted-foreground transition-base">
          {title}
        </CardTitle>
        <Icon className={cn("h-5 w-5 transition-base", iconStyles[variant])} />
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold transition-base">{value}</div>
        {description && (
          <p className="text-xs text-muted-foreground mt-1.5 leading-relaxed">{description}</p>
        )}
        {trend && (
          <p
            className={cn(
              "text-xs mt-2 font-medium transition-base",
              trend.isPositive ? "text-success" : "text-destructive"
            )}
          >
            {trend.value}
          </p>
        )}
      </CardContent>
    </Card>
  );
}
