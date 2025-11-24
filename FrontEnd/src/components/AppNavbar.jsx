import { Bell, User } from "lucide-react";
import { SidebarTrigger } from "@/components/ui/sidebar";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { useNavigate } from "react-router-dom";


export function AppNavbar({ userName = "User", role = "User" }) {
  const navigate = useNavigate();


  const handleLogout = () => {
    localStorage.removeItem("userRole");
    navigate("/login"); 
  };

  const getInitials = (name) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);
  };


    userName=JSON.parse(localStorage.getItem("user")).username;

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 transition-base">
      <div className="flex h-16 items-center gap-4 px-6">
        <SidebarTrigger className="-ml-2 transition-base" />
        
        <div className="flex-1" />

        

        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="relative h-10 gap-2 transition-base">
              <Avatar className="h-8 w-8 transition-base">
                <AvatarFallback className="bg-primary text-primary-foreground transition-base">
                  {getInitials(userName)}
                </AvatarFallback>
              </Avatar>
              <div className="flex flex-col items-start text-sm">
                <span className="font-medium transition-base">{userName}</span>
                <span className="text-xs text-muted-foreground transition-base">{role}</span>
              </div>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56 transition-base">
            <DropdownMenuLabel className="transition-base">My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem className="transition-base">
              <User className="mr-2 h-4 w-4 transition-base" />
              Profile
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={handleLogout} className="transition-base">
              Logout
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>
  );
}
