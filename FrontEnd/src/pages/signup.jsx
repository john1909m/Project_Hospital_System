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

export default function Signup() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("patient");
  const [patientName, setPatientName] = useState("");
  const [patientPhone, setPatientPhone] = useState("");
  const [patientGender, setPatientGender] = useState("");
  const [patientAge, setPatientAge] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();
  const { toast } = useToast();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    const payload = {
      username,
      email,
      password,
      patient: {
        patientName,
        patientPhone,
        patientGender,
        patientAge: Number(patientAge),
      },
    };

    try {
      const response = await fetch("http://localhost:8080/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      let data =null;
        try {
            const text = await response.text();
            data = text ? JSON.parse(text) : null;
        } catch (e) {
            data = null;
        }

      if (response.ok) {
        toast({
          title: "Signup Successful",
          description: "Your account has been created. Redirecting to login...",
        });

        setTimeout(() => navigate("/login"), 1500);
      } else {
        toast({
          title: "Signup Failed",
          description: data.message || "Unable to create account",
          variant: "destructive",
        });
      }
    } catch (error) {
      toast({
        title: "Signup Failed",
        description: "An error occurred. Please try again.",
        variant: "destructive",
      });
      console.error(error);
    }

    setIsLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary/5 via-background to-accent/5 p-4 transition-base">
      <Card className=" max-w-md shadow-xl transition-base hover:shadow-2xl">
        <CardHeader className="space-y-3 text-center transition-base">
          <div className="mx-auto w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center transition-base">
            <Activity className="h-6 w-6 text-primary transition-base" />
          </div>
          <CardTitle className="text-2xl font-bold transition-base">Sign Up</CardTitle>
          <CardDescription className="transition-base">
            Create your account to access the healthcare portal
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="username">Username</Label>
              <Input
                id="username"
                type="text"
                placeholder="john123"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                placeholder="john@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
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

            <div className="space-y-2">
              <Label htmlFor="patientName">Patient Name</Label>
              <Input
                id="patientName"
                type="text"
                placeholder="John Doe"
                value={patientName}
                onChange={(e) => setPatientName(e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="patientPhone">Phone</Label>
              <Input
                id="patientPhone"
                type="text"
                placeholder="01012345678"
                value={patientPhone}
                onChange={(e) => setPatientPhone(e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="patientGender">Gender</Label>
              <Input
                id="patientGender"
                type="text"
                placeholder="Male/Female"
                value={patientGender}
                onChange={(e) => setPatientGender(e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="patientAge">Age</Label>
              <Input
                id="patientAge"
                type="number"
                placeholder="30"
                value={patientAge}
                onChange={(e) => setPatientAge(e.target.value)}
                required
              />
            </div>

            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? "Creating Account..." : "Sign Up"}
            </Button>

                <p className="text-center">Have already Account?</p>
            <div className="flex justify-center">
                
            <Button className="bg-primary"><a href="/Login">Login</a></Button>
           </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
