import { Home, Users, UserCog, Calendar, FileText, Settings } from "lucide-react";
import { NavLink } from "react-router-dom";
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarHeader,
} from "@/components/ui/sidebar";
import { Activity } from "lucide-react";
import { cn } from "@/lib/utils";

const navigationConfig = {
  admin: [
    { title: "Dashboard", icon: Home, url: "/admin/dashboard" },
  ],
  doctor: [
    { title: "Dashboard", icon: Home, url: "/doctor/dashboard" },
  ],
  patient: [
    { title: "Dashboard", icon: Home, url: "/patient/dashboard" },
  ],
};

export function AppSidebar({ role }) {
  const items = navigationConfig[role] || [];

  return (
    <Sidebar>
      <SidebarHeader className="border-b border-border px-6 py-4">
        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary">
            <Activity className="h-5 w-5 text-primary-foreground" />
          </div>
          <div className="flex flex-col">
            <span className="text-lg font-semibold">HealthCare+</span>
            <span className="text-xs text-muted-foreground capitalize">{role} Portal</span>
          </div>
        </div>
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Navigation</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <NavLink
                      to={item.url}
                      className={({ isActive }) =>
                        cn(
                          "transition-base",
                          isActive ? "bg-accent text-accent-foreground" : ""
                        )
                      }
                    >
                      <item.icon className="h-4 w-4 transition-base" />
                      <span className="transition-base">{item.title}</span>
                    </NavLink>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  );
}
