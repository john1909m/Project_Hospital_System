import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Activity } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      const data = await response.json();

      if (response.ok) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("userRole", data.user.roles[0].roleName?.toLowerCase() || ""); 
        localStorage.setItem("userId", data.user.userId);
        localStorage.setItem("user", JSON.stringify(data.user));

        toast({
          title: "Login Successful",
          description: `Welcome back! Redirecting to ${localStorage.getItem("userRole")} dashboard...`,
        });
        const roleName=String(localStorage.getItem("userRole") || "").toLowerCase();
        console.log("Role Name:", roleName); // Debugging line
        navigate(`/${roleName}/dashboard`);
      } else {
        toast({
          title: "Login Failed",
          description: data.message || "Invalid credentials",
          variant: "destructive",
        });
      }
    } catch (error) {
      toast({
        title: "Login Failed",
        description: "An error occurred. Please try again.",
        variant: "destructive",
      });
    }

    setIsLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary/5 via-background to-accent/5 p-4 transition-base">
      <Card className="max-w-md shadow-xl transition-base hover:shadow-2xl">
        <CardHeader className="space-y-3 text-center transition-base">
          <div className="mx-auto w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center transition-base">
            <Activity className="h-6 w-6 text-primary transition-base" />
          </div>
          <CardTitle className="text-2xl font-bold transition-base">
            HealthCare+ Portal
          </CardTitle>
          <CardDescription className="transition-base">
            Sign in to access your healthcare dashboard
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="username">Username</Label>
              <Input
                id="username"
                type="text"
                placeholder="JohnDoe"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input
                id="password"
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? "Signing in..." : "Sign In"}
            </Button>
          </form>

          <p className="text-center mt-4">Don't have an account?</p>
          <div className="flex justify-center mt-2">
            <Button
              className="bg-primary"
              onClick={() => navigate("/signup")}
            >
              Signup
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
